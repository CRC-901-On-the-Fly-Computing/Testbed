package de.upb.crc901.otftestbed.buy_processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.buy_processor.impl.exceptions.CredentialVerifierNotInitializedException;
import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactCredentialVerifier;

@Component
public class CredentialVerifierComponent {

	private static final Logger logger = LoggerFactory.getLogger(CredentialVerifierComponent.class);

	@Autowired
	private PocConnector connect;

	private ReactCredentialVerifier verifier;

	private boolean isInitialized = false;


	public void initialize(ReactPublicParameters pp) {
		logger.debug("Requesting System Managers Public Identity ...");
		Helpers.setInjectableValue(ReactPublicParameters.class.toString(), pp);
		ReactSystemManagerPublicIdentity publicIdentity = connect.toMarketProvider().callSystemManager().getPublicIdentity();
		logger.debug("Got {}", publicIdentity.getRepresentation());
		logger.debug("Setting up Verifier");
		verifier = new ReactCredentialVerifier(pp, publicIdentity);
		isInitialized = true;
		logger.debug("Verifier initialized!");
	}

	public void deinitialize() {
		verifier = null;
		isInitialized = false;
	}

	public ReactCredentialVerifier getVerifier() {
		if (!isInitialized) {
			throw new CredentialVerifierNotInitializedException();
		} else {
			return verifier;
		}
	}
}
