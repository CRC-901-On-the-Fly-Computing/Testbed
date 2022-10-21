package de.upb.crc901.otftestbed.service_requester.impl.components;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.requester.ServiceState;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.issuer.credentials.Attributes;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.policy.PolicyBuilder;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.pseudonym.impl.react.ReactIdentity;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.ReactUser;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactCredentialNonInteractiveResponseHandler;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;
import de.upb.crypto.react.buildingblocks.attributes.AttributeNameValuePair;
import de.upb.crypto.react.buildingblocks.attributes.StringAttributeDefinition;

@Component
/**
 * Controller class that handles the authentication token generation of the user
 * towards the bought service.
 * 
 * On high level every token generation takes three steps:
 * 
 * First, you need to generate a credential request for the trusted third party
 * (TTP). After the TTP verified the request you have to finish the credential
 * generation. Finally, you can take the credential to generate a token for the
 * entity you want to authenticate against.
 * 
 * 
 * @author Mirko Jürgens
 *
 */
public class AccessTokenComponent {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenComponent.class);

	@Autowired
	private PocConnector connect;

	@Autowired
	private RestTemplate restTemplate;

	/* Describes the field in the credential which contains the hashOfOffer. */
	private static final String CREDENTIAL_KEY = "accessCredential";

	/* Used to register in the Rabbit-MessageQueue */
	@Autowired
	private ConnectionFactory connectionFactory;

	/*
	 * Maps an open credential-request to the corresponding request in the
	 * service-requester
	 */
	private Map<UUID, ReactCredentialNonInteractiveResponseHandler> requestToHandler = new HashMap<>();

	/*
	 * Used to asynchronously retrieve the session key in the message-queue handler.
	 */
	private Map<String, String> queueNameToSession = new HashMap<>();

	/*
	 * Used to asynchronously retrieve the service url in the message-queue handler.
	 */
	private Map<String, String> queueNameToRawService = new HashMap<>();

	@Autowired
	private DataHolderComponent dataHolder;

	Semaphore userSemaphore = new Semaphore(1);

	/**
	 * Creates a request for a credential of the item specified by the
	 * <code> hashOfOffer</code>. The market (or any other TTP) can grant this
	 * request upon completing the purchase of the item.
	 * 
	 * @param requestUUID the (unique) request in the service-requester such that
	 *                    multiple credential request can be issued at once.
	 * @param hashOfOffer the unique SHA-256 based hash of the offer
	 * @return a request that has to be granted by a TTP
	 */
	public ReactNonInteractiveCredentialRequest createCredential(UUID requestUUID, String hashOfOffer) {

		/* Every token needs an identity of the user. */
		ReactUser user = dataHolder.getUser();
		logger.debug("Committing to user identity...");
		ReactIdentity userIdentity = user.createIdentity();

		/* Retrieve the TTPs public key */
		logger.debug("Requesting access token issuer public identity ...");
		ReactCredentialIssuerPublicIdentity identity = connect.toMarketProvider().callBuyProcessor()
				.getAccessTokenIssuerPublicIdentity();

		/*
		 * Now, specify which attributes the TTP should grant us. In this case: We want
		 * to have an attribute `accessCredential` which contains the hash of the bought
		 * item, such that we can show to the service that the TTP confirmed the
		 * purchase of the item.
		 */

		StringAttributeDefinition accessAttr = (StringAttributeDefinition) identity.getAttributeSpace()
				.get(CREDENTIAL_KEY);
		AttributeNameValuePair anvp = accessAttr.createAttribute(hashOfOffer);
		Attributes attributes = new Attributes(new AttributeNameValuePair[] { anvp });

		/*
		 * Use the attribute-request and the TTPs public key to generate a credential
		 * request and return it.
		 */
		logger.debug("Creating access token issuance request ...");
		ReactCredentialNonInteractiveResponseHandler handler = user.createNonInteractiveIssueCredentialRequest(identity,
				userIdentity, attributes);

		/*
		 * Store the handler under the serviceUUID such that we can finish this request
		 * later.
		 */
		requestToHandler.put(requestUUID, handler);
		return (ReactNonInteractiveCredentialRequest) handler.getRequest();
	}

	/**
	 * Finishes the authentication credential request. Hence, the user has an
	 * attribute, signed by the TTP, that confirms the purchase.
	 * 
	 * This attribute is used to generate an access token for the bought service.
	 * Therefore, it will subscribe to a message-queue, known by the bought service,
	 * that notifies the user as soon as the the composed service is finished with
	 * booting such that the token generation can be finished.
	 * 
	 * @param response    the credential response
	 * @param requestUUID the request for this credential
	 * @param serviceURL  the original service url
	 * @return the url of the service
	 */
	public String receiveAccessTokenCredential(ReactCredentialIssueResponse response, UUID requestUUID,
			String serviceURL, String hashOfOffer) {

		/*
		 * Now, we generate a session-key that the user can use to show his token to the
		 * service.
		 */
		logger.debug("Successfully received the access token credential.");
		String sessionKey = UUID.randomUUID().toString();
		queueNameToRawService.put(hashOfOffer, serviceURL);
		queueNameToSession.put(hashOfOffer, sessionKey);

		/*
		 * As the service has not finished booting yet, we use a message queue under
		 * which the service notifies us when it is finished booting.
		 */
		logger.debug("subscribing to message_queue...");
		new Thread(() -> subscribeToRabbit(hashOfOffer, requestUUID, response)).start();

		/*
		 * Even if the token generation is done asynchronously via the message-queue, we
		 * already know which session key the user will use later, hence we can return
		 * it already.
		 */
		return new StringBuilder(serviceURL).append("/").append("?token=").append(sessionKey).toString();
	}

	/**
	 * Dynamically creates a listener for the message queue under which the service
	 * will announce that it has finished booting. Therefore, we register a
	 * {@link CredentialVerifierPIReceiver} at this queue.
	 * 
	 * @param queueName   the name of the queue we want to listen to (which is just
	 *                    the hash of the offer)
	 * @param requestUUID the unique request uuid such that we can set the token for
	 *                    tihs request as soon as we have finished generating it.
	 * @param response
	 */
	private void subscribeToRabbit(String queueName, UUID requestUUID, ReactCredentialIssueResponse response) {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("x-message-ttl", 600000L);
		Queue queue = new Queue(queueName, true, false, false, arguments);
		RabbitAdmin rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		FanoutExchange exchange = new FanoutExchange(queueName + "-exchange");

		Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, queueName + "-exchange", "", null);

		rabbitAdmin.declareQueue(queue);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareBinding(binding);

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(this.connectionFactory);
		container.setQueues(queue);
		CredentialVerifierPIReceiver receiver = new CredentialVerifierPIReceiver(container);
		receiver.queueName = queueName;
		receiver.requestUUID = requestUUID;
		receiver.response = response;
		MessageConverter messageConverter = new Jackson2JsonMessageConverter(Helpers.getMapper());
		container.setMessageListener(new MessageListenerAdapter(receiver, messageConverter));
		container.start();

	}

	/**
	 * Listener class that is used to finish the token generation as soon as the
	 * service has finished booting.
	 * 
	 * @author Mirko Jürgens
	 *
	 */
	class CredentialVerifierPIReceiver {

		/* The access token issuance reponse */
		ReactCredentialIssueResponse response;

		/* The uuid for this listeners request. */
		UUID requestUUID;

		/* The queue name which it listens on. */
		String queueName;

		SimpleMessageListenerContainer container;

		public CredentialVerifierPIReceiver(SimpleMessageListenerContainer container) {
			this.container = container;
		}

		/**
		 * Receives the announcement of the service that it has finished booting. This
		 * is done via announcing it's public key to all listeners.
		 * 
		 * @param pi the public key in serialized json.
		 */
		public void handleMessage(String pi) {
			/* Deserialize the public key. */
			Representation repr = new JSONConverter().deserialize(pi);
			ReactVerifierPublicIdentity reactVerifierPublicIdentity = new ReactVerifierPublicIdentity(repr,
					dataHolder.getPp());
			String serviceURL = queueNameToRawService.get(queueName);

			logger.debug("The message queue for request {}, hash {} & service_link {} received the message {}",
					requestUUID, queueName, serviceURL, pi);

			/* And use it to generate a token for this service */
			createAccessToken(serviceURL, queueName, reactVerifierPublicIdentity);
		}

		private void createAccessToken(String serviceURL, String hashOfOffer,
				ReactVerifierPublicIdentity servicePublicIdentity) {
			try {
				userSemaphore.acquire();
			} catch (InterruptedException e1) {
				logger.error("Interrrupted while waiting for user sem");
				Thread.currentThread().interrupt();
			}
			try {
				ReactUser user = dataHolder.getUser();
				/*
				 * First, look up the corresponding handler and finish the attribute granting
				 * process.
				 */
				logger.debug("Receiving access token credential...");
				ReactCredentialNonInteractiveResponseHandler handler = requestToHandler.get(requestUUID);
				user.receiveCredentialNonInteractively(handler, response);

				ReactPublicParameters pp = dataHolder.getPp();
				logger.debug("Creating access token...");

				/*
				 * Each token is associated to an user identity, we can just take a new one here
				 */
				logger.debug("Committing to user identity ...");
				ReactIdentity userIdentity = user.createIdentity();

				/* First, we need the TTPs public key */
				ReactCredentialIssuerPublicIdentity ttpPublicIdentity = connect.toMarketProvider().callBuyProcessor()
						.getAccessTokenIssuerPublicIdentity();

				/*
				 * Using this public key we now can generate a policy for our access token: In
				 * this policy we state that we do have the attribute `accessCredential` with
				 * the value <code> hashOfOffer</code>.
				 */
				logger.debug("Creating policy information...");
				PolicyInformation pi = PolicyBuilder.policy(pp).forIssuer(ttpPublicIdentity).attribute(CREDENTIAL_KEY)
						.isEqual(hashOfOffer).build();

				/*
				 * Using this policy, the public key of the TTP and the public key of the
				 * service, we can generate a token.
				 */
				ReactNonInteractivePolicyProof token = user.createNonInteractivePolicyProof(userIdentity, pi,
						servicePublicIdentity);
				logger.debug("Token created. sending it to the service");

				/*
				 * Now we want to show the service that the session we generated earlier is
				 * associated to the token generated here.
				 */
				String sessionKey = queueNameToSession.get(queueName);
				String putSessionURI = new StringBuilder("http://").append(serviceURL).append("/sessions/")
						.append(sessionKey).toString();
				String stringToken = new JSONConverter().serialize(token.getRepresentation());
				String encodedToken = new Base64().encodeToString(stringToken.getBytes(StandardCharsets.UTF_8));

				logger.debug("PUT URI {}", putSessionURI);
				/*
				 * Thus, we POST this token to the service and tell him that this token is
				 * associated to the session.
				 */
				restTemplate.put(putSessionURI, encodedToken);

				/*
				 * Now, update the models such that the user knows that we finished the
				 * authentication and the service is indeed running.
				 */
				logger.debug("Setting state to running");
				dataHolder.getBoughtItemForRequest(requestUUID).setAccessCredential(token);
				dataHolder.getBoughtItemForRequest(requestUUID).setServiceState(ServiceState.RUNNING);
				logger.debug("Successfully registered session at the service. Exiting container...");
				container.stop();
				userSemaphore.release();
			} catch (Exception e) {
				userSemaphore.release();
				throw e;
			}
		}
	}

}
