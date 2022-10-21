package de.upb.crc901.otftestbed.buy_processor.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.buy_processor.impl.config.BuyProcessorConfig;
import de.upb.crc901.otftestbed.buy_processor.impl.exceptions.AccessTokenIssuerNotInitializedException;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuer;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import de.upb.crypto.react.buildingblocks.attributes.StringAttributeDefinition;

/**
 * An issuer that issues single tokens for an bought item. For example, if the
 * user buys the offer with the uuid: "abc-cde-fgh" and then the issuer will
 * issue a single credential 'abc-cde-fgh'. The user can use this credential to
 * generate a token, when he wants to use this service. The host can then
 * verify, if the user is allowed to use this service composition at this host
 * (i.e. if he really bought the item).
 * 
 * @author mirkoj
 *
 */
@Component
public class AccessTokenIssuerComponent {
	private ReactCredentialIssuer issuer;

	private boolean isInitialized = false;

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenIssuerComponent.class);

	@Autowired
	private BuyProcessorConfig config;

	public void initialize(ReactPublicParameters pp) {
		logger.debug("Checking whether the review token issuer is presisted...");
		if (config.isPersistent()) {
			logger.debug("Trying to read the issuer from the stored file...");
			String fileName = BuyProcessorConfig.ACCESS_TOKEN_ISSUER_FILENAME;

			// public parameters
			try (BufferedReader fis = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {

				String nextLine = fis.readLine();
				String marshalledObject = "";
				while (nextLine != null) {
					marshalledObject = marshalledObject + nextLine + "\n";
					nextLine = fis.readLine();
				}
				// convert it to representation
				JSONConverter converter = new JSONConverter();
				Representation repr = converter.deserialize(marshalledObject);
				issuer = new ReactCredentialIssuer(repr);
			} catch (IOException e) {
				logger.error("Failed to read from file even though it was required, using new keys...", e);
				issuer = new ReactCredentialIssuer(pp,
						Arrays.asList(new StringAttributeDefinition("accessCredential", ".*")));
			}
		} else {
			logger.debug("Generating new issuer...");
			issuer = new ReactCredentialIssuer(pp,
					Arrays.asList(new StringAttributeDefinition("accessCredential", ".*")));
		}
		isInitialized = true;
	}

	public void deinitialize() {
		issuer = null;
		isInitialized = false;
	}
	
	public ReactCredentialIssueResponse requestCredential(ReactNonInteractiveCredentialRequest body) {
		ReactCredentialIssuer reactIssuer = this.getIssuer();
		logger.debug("Got credential request for " + body.getRepresentation().toString());
		ReactCredentialIssueResponse response = reactIssuer.issueNonInteractively(body);
		logger.debug("Returning " + response.getRepresentation().toString());
		return response;
	}

	public boolean isIntialized() {
		return isInitialized;
	}

	public ReactCredentialIssuer getIssuer() {
		if (!isInitialized) {
			throw new AccessTokenIssuerNotInitializedException();
		} else {
			return issuer;
		}
	}
}
