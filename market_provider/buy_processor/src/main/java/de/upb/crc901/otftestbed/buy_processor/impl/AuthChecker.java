package de.upb.crc901.otftestbed.buy_processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.verifier.credentials.VerificationResult;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

@Component
public class AuthChecker {

	@Autowired
	private CredentialVerifierComponent verifier;

	private static final Logger logger = LoggerFactory.getLogger(AuthChecker.class);

	/**
	 * Validates an authentication token. <br>
	 * 1) Get the policy information from the otf-policy-provider for this
	 * service-composition <br>
	 * 2) Verifies this authentication token with the CredentialVerifier using the
	 * service-composition's policy.
	 * 
	 * @param serviceCompositionIdentifier
	 *            the service-composition-id
	 * @param authenticationToken
	 *            the proof of the user that he indeed is authorized to use buy such
	 *            a composition
	 * @param pi
	 * @return a verification result (e.g. whether the user is or is not authorized
	 *         to buy this item)
	 */
	public VerificationResult validateAuthenticationToken(String serviceCompositionIdentifier,
			ReactNonInteractivePolicyProof authenticationToken, PolicyInformation pi) {
		logger.debug("Requesting the required policy information for this service composition...");
	//	logger.debug("Policy information is {}", pi.getRepresentation());
	//	logger.debug("Master cred is required: {}", pi.isMasterCredentialRequired());
	//	logger.debug("Received Master cred is: {}", authenticationToken.getMasterCredential());
		logger.debug("Verifying the authentication token...");
		return verifier.getVerifier().verifyNonInteractiveProof(authenticationToken, pi);
	}

	public void initialize(ReactPublicParameters pp) {
		verifier.initialize(pp);
	}

	public void deinitialize() {
		verifier.deinitialize();
	}

	public ReactVerifierPublicIdentity getPublicIdentity() {
		return verifier.getVerifier().getIdentity();
	}
}
