package de.upb.crc901.otftestbed.service_requester.impl.controllers;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.reputation.BuyResponse;
import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crc901.otftestbed.service_requester.impl.components.AccessTokenComponent;
import de.upb.crc901.otftestbed.service_requester.impl.components.AuthenticatorComponent;
import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;
import de.upb.crc901.otftestbed.service_requester.impl.components.RaterComponent;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoReviewTokenFoundException;
import de.upb.crc901.otftestbed.service_requester.impl.models.WaitingForOfferData;
import de.upb.crypto.react.acs.issuer.credentials.Attributes;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactRepresentableReviewToken;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewToken;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactJoinResponse;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractiveJoinRequest;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.ReactUser;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import de.upb.crypto.react.acs.user.impl.react.reviewtoken.ReactNonInteractiveReviewTokenRequest;
import de.upb.crypto.react.acs.user.impl.react.reviewtoken.ReactReviewTokenNonInteractiveResponseHandler;

@Component
public class MarketController {

	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);

	@Autowired
	private PocConnector connect;

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private DataHolderComponent holder;

	@Autowired
	private AuthenticatorComponent authenticator;

	@Autowired
	private RaterComponent rater;

	@Autowired
	private AccessTokenComponent accessTokenComponent;

	//@EventListener(ApplicationReadyEvent.class)
	public void bootstrapUserRegistration(ApplicationReadyEvent applicationReadyEvent) {
		SimpleAttributes simpleAttributes = new SimpleAttributes();
		Map<String, Object> attributePairs = new HashMap<>();
		attributePairs.put("country", "USA");
		attributePairs.put("subscriptionLevel", "Prime");
		attributePairs.put("userLicense", "Private");
		attributePairs.put("age", "20");

	    simpleAttributes.setAttributePairs(attributePairs);
		registerUser(simpleAttributes);
	}
	
	
	public ResponseEntity<SimpleJSONMessage> registerUser(SimpleAttributes body) {
		logger.debug("Got register request for attributes: {} ", body.getAttributePairs());

		logger.debug("Setting up the service requester...");
		holder.setPp(connect.toMarketProvider().callSystemManager().getPublicParameter());

		logger.debug("Retrieving Credential Issuer public Identity");
		ReactCredentialIssuerPublicIdentity credPublicIdentity = connect.toMarketProvider().callCredentialIssuer()
				.getPublicIdentity();

		Attributes userAttributes = authenticator.generateAttributesForUser(body, credPublicIdentity);

		logger.debug("Requesting system manager public identity...");
		Helpers.setInjectableValue(ReactPublicParameters.class.toString(), holder.getPp());
		ReactSystemManagerPublicIdentity publicIdentity = connect.toMarketProvider().callSystemManager()
				.getPublicIdentity();
		logger.debug("Creating user...");
		holder.setUser(new ReactUser(holder.getPp()));
		logger.debug("Creating join request...");
		ReactNonInteractiveJoinRequest joinRequest = holder.getUser().createNonInteractiveJoinRequest(publicIdentity);
		logger.debug("Sending join request to the system manager...");
		ReactJoinResponse joinResponse = connect.toMarketProvider().callSystemManager().joinUser(joinRequest);
		logger.debug("Finishing registration...");
		holder.getUser().finishRegistration(joinResponse);
		logger.debug("Initializing credentials...");
		authenticator.createCredential(holder.getUser(), userAttributes);
		holder.setRegistered(true);

		//telemetry.logRequesterAdded(String.valueOf(this.hashCode()));

		return new ResponseEntity<>(new SimpleJSONMessage("User successfully registered."), HttpStatus.CREATED);

	}

	public ResponseEntity<SimpleJSONMessage> buyOffer(UUID requestUUID, UUID offerUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		logger.debug("Checking whether there is such a offer...");

		WaitingForOfferData waitingForOfferData = holder.getWaitingForOfferData(requestUUID);

		UUID otfpUUID = waitingForOfferData.getProsecoInterviewData().getSelectedOTFProvider().getOtfpUUID();
		Offer offer = connect.toMarketProvider().callBuyProcessor().getOffer(otfpUUID, requestUUID, offerUUID);

		logger.debug("Offer is {}", offer);

		PolicyInformation pi = offer.getPolicyInformation();
		offer.setPolicyInformation(null);
		logger.debug("Buying {} ", offerUUID);
		byte[] itemBytes;
		try {

			String itemAsJson = Helpers.getMapper().writeValueAsString(offer);
			itemAsJson = itemAsJson.trim();
			logger.debug("Length of item as json is {}", itemAsJson.length());
			itemBytes = itemAsJson.getBytes(StandardCharsets.UTF_8);
			logger.debug("Length of item as bytes is {}", itemBytes.length);

		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}

		byte[] hashOfOfferedItem = holder.getPp().getHashIntoZp().hashIntoStructure(itemBytes)
				.getUniqueByteRepresentation();

		offer.setPolicyInformation(pi);

		ReactNonInteractivePolicyProof authToken = authenticator.createAuthenticationToken(holder.getUser(), pi,
				holder.getPp());
		logger.debug("Committing to user identity...");

		ReactReviewTokenNonInteractiveResponseHandler reviewTokenHandler = rater.createReviewToken(hashOfOfferedItem,
				holder.getPp(), holder.getUser());

		logger.debug("Creating access token request...");

		String hashOfOffer = new Base64().encodeAsString(hashOfOfferedItem);

		logger.debug("Length of base 64 encoded offer is {}", hashOfOffer.length());

		ReactNonInteractiveCredentialRequest accessRequest = accessTokenComponent.createCredential(requestUUID,
				hashOfOffer);

		BuyTokens tokens = new BuyTokens(authToken,
				(ReactNonInteractiveReviewTokenRequest) reviewTokenHandler.getRequest(), accessRequest);

		logger.debug("Trying to buy via buy-processor...");

		BuyResponse response = connect.toMarketProvider().callBuyProcessor().buyItem(tokens, otfpUUID, requestUUID,
				offerUUID);

		logger.debug("Processing the access-token...");
		String serviceLink = accessTokenComponent.receiveAccessTokenCredential(response.getAccessToken(), requestUUID,
				response.getServiceLink(), hashOfOffer);

		logger.debug("Processing review token response...");

		holder.getUser().receiveReviewTokenNonInteractively(reviewTokenHandler, response.getReviewToken());
		ReactRepresentableReviewToken representableToken = reviewTokenHandler.getReceiver()
				.receive(response.getReviewToken());
		holder.finishPhaseFourOfRequest(requestUUID, offer, serviceLink);
		ReactReviewToken rrt = representableToken.getReviewToken(holder.getPp());
		holder.setReviewTokenForUUID(requestUUID, rrt);

		logger.debug("Returning to caller");
		return new ResponseEntity<>(new SimpleJSONMessage(serviceLink), HttpStatus.CREATED);

	}

	public ResponseEntity<SimpleJSONMessage> putRating(UUID requestName, ServiceReputation body) {
		logger.debug("Trying to rate service...");
		logger.debug("Looking for review token...");
		ReactReviewToken token = holder.getReviewTokenForUUID(requestName);
		if (token == null)
			throw new NoReviewTokenFoundException();
		logger.debug("Delegating rating process to rater...");
		Offer item = holder.getBoughtOfferForRequest(requestName);
		SimpleJSONMessage toReturn = rater.createRating(body, token, holder.getUser(), item);
		logger.debug("Returning to caller...");
		return new ResponseEntity<>(toReturn, HttpStatus.CREATED);
	}
}
