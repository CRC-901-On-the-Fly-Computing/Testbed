package de.upb.crc901.otftestbed.policy_provider.impl;

import static de.upb.crypto.react.acs.policy.PolicyBuilder.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.policy_provider.generated.spring_server.api.PolicyProviderApiDelegate;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;

@Component
public class PolicyProviderControllerDelegate implements PolicyProviderApiDelegate {

	private static final Logger log = LoggerFactory.getLogger(PolicyProviderControllerDelegate.class);

	private PocConnector connect;

	private boolean isInitialized = false;

	private ReactPublicParameters pp;


	@Autowired
	public PolicyProviderControllerDelegate(PocConnector connect) {
		Assert.notNull(connect, "connect must not be null");

		this.connect = connect;
	}


	public ResponseEntity<String> initialize() {
		if (isInitialized) {
			return new ResponseEntity<>("Already initialized.", HttpStatus.OK);

		} else {
			log.debug("Requesting PublicParameters via the SystemManager...");
			pp = connect.toMarketProvider().callSystemManager().getPublicParameter();

			isInitialized = true;

			return new ResponseEntity<>("Successfully initialized.", HttpStatus.OK);
		}
	}

	public ResponseEntity<String> deinitialize() {
		if (!isInitialized) {
			return new ResponseEntity<>("Already deinitialized.", HttpStatus.OK);

		} else {
			pp = null;
			isInitialized = false;

			return new ResponseEntity<>("Successfully deinitialized.", HttpStatus.OK);
		}
	}

	private ReactPublicParameters getPublicParameters() {
		if (!isInitialized) {
			initialize();
		}

		return pp;
	}

	@Override
	public ResponseEntity<PolicyInformation> getServiceCompositionPolicy(String serviceCompositionId) {

		log.debug("Retrieving CredentialIssuer Public identity ... ");
		ReactCredentialIssuerPublicIdentity identity = connect.toMarketProvider().callCredentialIssuer().getPublicIdentity();

		log.debug("Constructing policy...");
		PolicyInformation pi = policy(this.getPublicParameters()).forIssuer(identity).attribute("age")
				.isInRange(18, 35).attribute("country").isInSet("Germany", "USA", "Canada")
				.attribute("subscriptionLevel").isInSet("Prime", "Premium").attribute("userLicense")
				.isInSet("Private", "Business").build();

		return new ResponseEntity<>(pi, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PolicyInformation> getServicePolicy(String serviceName) {

		log.debug("Retrieving CredentialIssuer Public identity ... ");
		ReactCredentialIssuerPublicIdentity identity = connect.toMarketProvider().callCredentialIssuer().getPublicIdentity();

		log.debug("Constructing policy...");
		PolicyInformation pi = policy(this.getPublicParameters()).forIssuer(identity).attribute("age")
				.isInRange(18, 35).attribute("country").isInSet("Germany", "USA", "Canada")
				.attribute("subscriptionLevel").isInSet("Prime", "Premium").attribute("userLicense")
				.isInSet("Private", "Business").build();

		return new ResponseEntity<>(pi, HttpStatus.OK);
	}
}
