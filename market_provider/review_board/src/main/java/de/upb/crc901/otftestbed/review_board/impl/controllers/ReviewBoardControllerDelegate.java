package de.upb.crc901.otftestbed.review_board.impl.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.ReputationAndItem;
import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputationAndSignature;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crc901.otftestbed.review_board.generated.spring_server.api.ReviewBoardApiDelegate;
import de.upb.crc901.otftestbed.review_board.impl.exception.DuplicateReviewException;
import de.upb.crc901.otftestbed.review_board.impl.exception.InvalidReviewException;
import de.upb.crc901.otftestbed.review_board.impl.exception.VerificationFailedException;
import de.upb.crc901.otftestbed.review_board.impl.repositories.ReviewRepository;
import de.upb.crypto.enc.sym.streaming.aes.ByteArrayImplementation;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;
import de.upb.crypto.react.acs.review.impl.react.ReactReview;

@Component
public class ReviewBoardControllerDelegate implements ReviewBoardApiDelegate {

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	ReviewVerifierComponent verifier;

	@Autowired
	ReviewRepository repo;

	@Autowired
	AggregatorComponent aggregator;

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReviewBoardControllerDelegate.class);


	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}


	@SuppressWarnings("squid:S1166")
	private boolean isRatingValid(SearchableSignedReputation ssr) {
		try {
			return verifier.verifyMessage(ssr.getSignature(), ssr.getCompositionAndOtfp(), ssr.getItemName());
		} catch (InvalidReviewException e) {
			return false;
		}
	}

	public ResponseEntity<SimpleJSONMessage> initialize() {
		verifier.initialize();
		return new ResponseEntity<>(new SimpleJSONMessage("Successfully initialized."), HttpStatus.CREATED);
	}

	public ResponseEntity<SimpleJSONMessage> deinitialize() {
		verifier.deinitialize();
		return new ResponseEntity<>(new SimpleJSONMessage("Successfully deinitialized."), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> rateServiceComposition(ReactRepresentableReview body, String serviceCompositionID) {

		ReactReview review = verifier.getReview(body);
		ByteArrayImplementation byteMessage = review.getMessage();
		ReputationAndItem reputationAndItem;
		try {
			reputationAndItem = Helpers.getMapper().readValue(byteMessage.getData(), ReputationAndItem.class);
		} catch (IOException e) {
			log.error("Failed,", e);
			throw new IllegalArgumentException(e);
		}
		boolean isReviewVerified = verifier.verifyMessage(review, reputationAndItem.getOffer().getCompositionAndOTFProvider(), serviceCompositionID);
//		telemetry.logAuthenticationReview(isReviewVerified, reputationAndItem.getReputation().getReputationMessage());
		if (!isReviewVerified) {
			throw new VerificationFailedException();
		}
		log.debug("Verification of the signature was successful!");

		log.debug("Checking the database for duplicates.");
		List<SearchableSignedReputation> allReviews = repo.findReputationsByServiceCompositionId(serviceCompositionID);
		if (verifier.checkForDuplicates(review, allReviews)) {
			throw new DuplicateReviewException();
		}

		SearchableSignedReputation toStore = new SearchableSignedReputation(body, reputationAndItem.getReputation());
		log.debug("Storing the review in the database...");
		log.debug("Setting the item-name for easy searchability...");

		CompositionAndOTFProvider item = reputationAndItem.getOffer().getCompositionAndOTFProvider();
		toStore.setItemName(item.getSimpleComposition().getServiceCompositionId());
		toStore.setServices(item.getSimpleComposition().getServiceId());
		toStore.setOtfpUUID(item.getOtfpUUID());
		toStore.getReputation().setDate(System.currentTimeMillis());
		repo.save(toStore);
//		telemetry.logReputation(toStore);

		return new ResponseEntity<>(new SimpleJSONMessage("Successfully saved the review."), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> countAllServiceCompositionReviews() {
		String number = Long.toString(repo.countAllReputations());

		return new ResponseEntity<>(new SimpleJSONMessage(number), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> countServiceCompositionReviews(String serviceCompositionID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		String number = Long.toString(repo.countReputationsByServiceCompositionId(
				serviceCompositionID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability));

		return new ResponseEntity<>(new SimpleJSONMessage(number), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> countServiceReviews(String serviceID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		String number = Long.toString(repo.countReputationsByServiceId(
				serviceID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability));

		return new ResponseEntity<>(new SimpleJSONMessage(number), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ReactRepresentableReview>> getServiceCompositionRawReviewList(String serviceCompositionID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceCompositionId(
				serviceCompositionID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability);

		List<ReactRepresentableReview> toReturn = rawRatings.stream()
				// return only the signed part
				.map(SearchableSignedReputation::getSignature)
				.collect(Collectors.toList());

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ServiceReputationAndSignature>> getServiceCompositionReviewList(String serviceCompositionID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceCompositionId(
				serviceCompositionID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability);

		List<ServiceReputationAndSignature> toReturn = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// return only the service reputations
				.map( ssr -> new ServiceReputationAndSignature(ssr.getExtendedReputation(), ssr.getSignature()))
				.collect(Collectors.toList());

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ReactRepresentableReview>> getServiceReviewList(String serviceID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceId(
				serviceID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability);

		List<ReactRepresentableReview> toReturn = rawRatings.stream()
				// return only the signed part
				.map(SearchableSignedReputation::getSignature)
				.collect(Collectors.toList());

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ServiceReputationAndSignature>> getServiceReputationList(String serviceID, Integer maxOverall, Integer maxOther, Integer maxPerformance, Integer maxSecurity, Integer maxUsability) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceId(
				serviceID, maxOverall, maxOther, maxPerformance, maxSecurity, maxUsability);

		List<ServiceReputationAndSignature> toReturn = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// return only the service reputations
				.map( ssr -> new ServiceReputationAndSignature(ssr.getExtendedReputation(), ssr.getSignature()))
				.peek(e -> e.getServiceReputation().setServiceCompositionId(serviceID))
				.collect(Collectors.toList());

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ExtendedServiceReputation> getAggregatedServiceCompositionReputation(String serviceCompositionID) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceCompositionId(
				serviceCompositionID);

		ExtendedServiceReputation toReturn = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// get service reputations
				.map(SearchableSignedReputation::getExtendedReputation)
				// aggregate the service reputations
				.collect(Collectors.collectingAndThen(Collectors.toList(), aggregator::aggregate));

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ExtendedServiceReputation> getAggregatedServiceReputation(String serviceID) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByServiceId(
				serviceID);

		ExtendedServiceReputation toReturn = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// get service reputations
				.map(SearchableSignedReputation::getExtendedReputation)
				.peek(e -> e.setServiceCompositionId(serviceID))
				// aggregate the service reputations
				.collect(Collectors.collectingAndThen(Collectors.toList(), aggregator::aggregate));

		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ExtendedServiceReputation>> getAggregatedServiceCompositionReviewList() {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.getAllReputations();

		Collection<ExtendedServiceReputation> ratingCollection = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// group by and aggregate for each service composition ID
				.collect(
						// group by service composition ID
						Collectors.groupingBy(SearchableSignedReputation::getItemName,
						// map to ServiceReputation
						Collectors.mapping(SearchableSignedReputation::getExtendedReputation,
						// collect into a list and aggregate it for each service composition ID
						Collectors.collectingAndThen(Collectors.toList(), aggregator::aggregate))))
				.values();

		List<ExtendedServiceReputation> toReturn = new ArrayList<>(ratingCollection);
		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ExtendedServiceReputation> getOtfProviderRating(UUID otfpUUID) {

		// get all signed ratings from the repository
		List<SearchableSignedReputation> rawRatings = repo.findReputationsByOtfpUUID(otfpUUID);

		ExtendedServiceReputation toReturn = rawRatings.stream()
				// remove invalid ratings
				.filter(this::isRatingValid)
				// get service reputations
				.map(SearchableSignedReputation::getExtendedReputation)
				// aggregate the service reputations
				.collect(Collectors.collectingAndThen(Collectors.toList(), aggregator::aggregate));

		return ResponseEntity.ok(toReturn);
	}
}
