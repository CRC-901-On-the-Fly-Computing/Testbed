package de.upb.crc901.otftestbed.service_requester.impl.components;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.upb.crc901.otftestbed.commons.reputation.ReputationAndItem;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputation;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.DuplicateReviewException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InvalidReviewException;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuerPublicIdentity;
import de.upb.crypto.react.acs.issuer.reviewtokens.ReviewToken;
import de.upb.crypto.react.acs.pseudonym.impl.react.ReactIdentity;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;
import de.upb.crypto.react.acs.review.impl.react.ReactReview;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.ReactUser;
import de.upb.crypto.react.acs.user.impl.react.reviewtoken.ReactReviewTokenNonInteractiveResponseHandler;

@Component
public class RaterComponent {

	private static final Logger logger = LoggerFactory.getLogger(RaterComponent.class);

	@Autowired
	private PocConnector connect;


	public ReactReviewTokenNonInteractiveResponseHandler createReviewToken(byte[] hashOfItem, ReactPublicParameters pp,
			ReactUser user) {
		logger.debug("Requesting review token issuer public identity...");
		ReactReviewTokenIssuerPublicIdentity pi = connect.toMarketProvider().callBuyProcessor()
				.getReviewTokenIssuerPublicIdentity();

		logger.debug("Committing to identity...");
		ReactIdentity userIdentity = user.createIdentity();
		logger.debug("Creating review token request");
		ReactReviewTokenNonInteractiveResponseHandler toReturn = user.createNonInteractiveIssueReviewTokenRequest(pi,
				userIdentity, hashOfItem);
		logger.debug("Returning...");
		return toReturn;
	}

	public SimpleJSONMessage createRating(ServiceReputation rating, ReviewToken reviewToken, ReactUser user, Offer boughtOffer) {
		logger.debug("Creating review...");
		
		ReputationAndItem reputationAndItem = new ReputationAndItem(rating, boughtOffer);
		
		byte[] message;
		try {
			message = Helpers.getMapper().writeValueAsBytes(reputationAndItem);
		} catch (JsonProcessingException e1) {
			throw new IllegalArgumentException(e1);
		}
		
		ReactReview review = user.createReview(message, reviewToken);
		ReactRepresentableReview repr = new ReactRepresentableReview(review);
		logger.debug("Sending rating to the review board ...");
		SimpleJSONMessage response = null;
		try {
			response = connect.toMarketProvider().callReviewBoard().rateServiceComposition(repr, boughtOffer.getCompositionID());
		} catch (HttpServerErrorException e) {
			if (e.getResponseBodyAsString().contains("The user has used this review token already.")) {
				throw new DuplicateReviewException();
			}
			if (e.getResponseBodyAsString()
					.contains("The service id given in the path does not match the service id of the issued token.")) {
				throw new InvalidReviewException();
			}
			logger.error(e.getResponseBodyAsString());
			throw e;
		}
		logger.debug("Review board returned " + response.getMessage());
		logger.debug("Returning...");
		return response;
	}
}
