package de.upb.crc901.otftestbed.credential_issuer.impl.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.credential_issuer.generated.spring_server.api.CredentialIssuerApiDelegate;
import de.upb.crc901.otftestbed.credential_issuer.impl.CredentialIssuerComponent;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuer;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;

@Component
public class CredentialIssuerControllerDelegate implements CredentialIssuerApiDelegate {

	private static final Logger log = LoggerFactory.getLogger(CredentialIssuerControllerDelegate.class);

	private final CredentialIssuerComponent ciComponent;

	private ReactCredentialIssuer credentialIssuer;

	private boolean isInitialized = false;


	@Autowired
	public CredentialIssuerControllerDelegate(CredentialIssuerComponent ciComponent) {
		Assert.notNull(ciComponent, "ciComponent must not be null");

		this.ciComponent = ciComponent;
	}


	public ResponseEntity<String> initialize() {
		if (isInitialized()) {
			return new ResponseEntity<>("Already initialized", HttpStatus.OK);

		} else {
			log.debug("Initializing...");

			this.credentialIssuer = ciComponent.getCredentialIssuer();
			// if no issuer is loaded, generate one
			if (this.credentialIssuer == null) {
				this.credentialIssuer = ciComponent.generateCredentialIssuer();
			}

			this.isInitialized = true;

			return new ResponseEntity<>("Initialization successful!", HttpStatus.OK);
		}
	}

	public ResponseEntity<String> deinitialize() {
		if (!isInitialized()) {
			return new ResponseEntity<>("Already deinitialized", HttpStatus.OK);

		} else {
			log.debug("Deinitializing...");

			this.credentialIssuer = null;
			this.isInitialized = false;

			return new ResponseEntity<>("Deinitialization successful!", HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<ReactCredentialIssuerPublicIdentity> getPublicIdentity() {
		return new ResponseEntity<>(getCredentialIssuer().getPublicIdentity(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ReactCredentialIssueResponse> requestCredential(ReactNonInteractiveCredentialRequest body) {
		log.debug("Got credential request for {}", body.getRepresentation());

		ReactCredentialIssueResponse response = getCredentialIssuer().issueNonInteractively(body);
		log.debug("Returning {}", response.getRepresentation());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public ReactCredentialIssuer getCredentialIssuer() {
		if (!isInitialized()) {
			initialize();
			// throw new CredentialIssuerNotInitializedException();
		}

		return credentialIssuer;
	}
}
