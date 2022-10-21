package de.upb.crc901.otftestbed.buy_processor.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.buy_processor.impl.config.BuyProcessorConfig;
import de.upb.crc901.otftestbed.buy_processor.impl.exceptions.ReviewTokenIssuerNotInitializedException;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuer;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;

@Component
public class ReviewTokenIssuerComponent {

	private ReactReviewTokenIssuer issuer;

	private boolean isInitialized = false;

	private static final Logger logger = LoggerFactory.getLogger(ReviewTokenIssuerComponent.class);
	@Autowired
	private BuyProcessorConfig config;

	public void initialize(ReactPublicParameters pp) {
		logger.debug("Checking whether the review token issuer is presisted...");
		if (config.isPersistent()) {
			logger.debug("Trying to read the issuer from the stored file...");
			String fileName = BuyProcessorConfig.REVIEW_TOKEN_ISSUER_FILENAME;

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
				issuer = new ReactReviewTokenIssuer(repr);
			} catch (IOException e) {
				logger.error("Failed to read from file even though it was required, using new keys...", e);
				issuer = new ReactReviewTokenIssuer(pp);
			}
		} else {
			logger.debug("Generating new issuer...");
			issuer = new ReactReviewTokenIssuer(pp);
		}
		isInitialized = true;
	}

	public void deinitialize() {
		issuer = null;
		isInitialized = false;
	}

	public ReactReviewTokenIssuer getReviewTokenIssuer(ReactPublicParameters pp) {
		if (!isInitialized) {
			initialize(pp);
		}
		return issuer;

	}
}
