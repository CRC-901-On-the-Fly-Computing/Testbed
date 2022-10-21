package de.upb.crc901.otftestbed.review_board.impl.controllers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crc901.otftestbed.review_board.impl.exception.InvalidReviewException;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuerPublicIdentity;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;
import de.upb.crypto.react.acs.review.impl.react.ReactReview;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.verifier.impl.react.reviews.ReactReviewVerifier;

@Component
public class ReviewVerifierComponent {

	private static final Logger log = LoggerFactory.getLogger(ReviewVerifierComponent.class);

	@Autowired
	private PocConnector connect;

	private ReactReviewVerifier reviewVerifier;

	private boolean isInitialized = false;

	private ReactPublicParameters pp;

	public void initialize() {
		if (!isInitialized) {
			log.debug("Requesting public parameters from the system manager...");
			pp = connect.toMarketProvider().callSystemManager().getPublicParameter();
			log.debug("Requesting PublicIdentity from the SystemManager...");
			Helpers.setInjectableValue(ReactPublicParameters.class.toString(), pp);
			ReactSystemManagerPublicIdentity publicIdentity = connect.toMarketProvider().callSystemManager()
					.getPublicIdentity();
			log.debug("Requesting the Review token issuer Public identity...");
			ReactReviewTokenIssuerPublicIdentity rtiPi = connect.toMarketProvider().callBuyProcessor()
					.getReviewTokenIssuerPublicIdentity();
			log.debug("Initializing review verifier...");
			reviewVerifier = new ReactReviewVerifier(pp, publicIdentity, rtiPi);
			isInitialized = true;
		}
	}

	public void deinitialize() {
		if (isInitialized) {
			isInitialized = false;
			pp = null;
			reviewVerifier = null;
		}
	}

	public boolean verifyMessage(ReactRepresentableReview review, CompositionAndOTFProvider serviceCompositionId,
			String compositionID) {
		if (!isInitialized) {
			initialize();
			// throw new ReviewBoardNotInitializedException();
		}
		return verifyMessage(review.getReview(pp), serviceCompositionId, compositionID);
	}

	public boolean verifyMessage(ReactReview review, CompositionAndOTFProvider item, String compositionID) {
		if (!isInitialized) {
			initialize();
			// throw new ReviewBoardNotInitializedException();
		}

		if (item.getOtfpUUID() == null || item.getSimpleComposition() == null
				|| !item.getSimpleComposition().getServiceCompositionId().equals(compositionID)) {

			throw new InvalidReviewException();
		}
		log.debug("Trying to parse the review...");
		log.debug("Parsing successful. Verifying the review...");
		log.debug("The contained item is: {}", item);
		return reviewVerifier.verify(review);
	}

	/**
	 * Returns true if there is an message from this user in the db, false
	 * otherwise.
	 * 
	 * @param review
	 * @param content
	 * @return
	 */
	public boolean checkForDuplicates(ReactReview review, List<SearchableSignedReputation> content) {
		if (!isInitialized)
			initialize();
		// throw new ReviewBoardNotInitializedException();
		log.debug("Parsing successful!");
		log.debug("Received {} messages from the database.", content.size());
		for (SearchableSignedReputation other : content) {
			log.debug("Trying to parse a review from the database...");
			ReactReview otherReview = other.getSignature().getReview(pp);
			if (reviewVerifier.areFromSameUser(review, otherReview)) {
				log.warn("Reviews are from same user. The reviews are {} and {}", review.getItem().getData().getData(),
						otherReview.getItem().getData().getData());
			}
			if (reviewVerifier.areFromSameUser(review, otherReview) && Arrays.equals(review.getItem().getData().getData(), otherReview.getItem().getData().getData())) {
				log.warn("Duplicate found. Returning error-message!");
				return true;
			}
			log.debug("Not a duplicate. Continuing...");
		}
		log.debug("No duplicates found!");
		return false;
	}

	public ReactReview getReview(ReactRepresentableReview body) {
		if (!isInitialized)
			initialize();
		// throw new ReviewBoardNotInitializedException();
		log.debug("Trying to parse the review.");
		return body.getReview(pp);
	}
}
