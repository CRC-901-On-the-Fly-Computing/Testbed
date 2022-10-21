package de.upb.crc901.otftestbed.service_requester.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.commons.general.JobState;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.commons.requester.ConfigurationMarketMonitorSources;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse;
import de.upb.crc901.otftestbed.commons.requester.ItemAndRequest;
import de.upb.crc901.otftestbed.commons.requester.JobstateAndRequest;
import de.upb.crc901.otftestbed.commons.requester.OTFProviderConfidence;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.OfferAndRequest;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONUuid;
import de.upb.crc901.otftestbed.service_requester.generated.spring_server.api.ServiceRequesterApiDelegate;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.DataManagementController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.InitialInterviewController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.MarketController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.OtfProviderController;

/**
 * The API-Gateway
 * @author Mirko Jürgens
 * 
 *
 */
@Component
public class ServiceRequesterControllerDelegate implements ServiceRequesterApiDelegate {
	
	@Autowired
	private DataManagementController dataController;

	@Autowired
	private MarketController marketController;
	
	@Autowired
	private InitialInterviewController initialInterviewController;

	@Autowired
	private OtfProviderController otfProviderController;


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


	/**
	 * Such that we don't have to call registerUser everytime
	 */
	//@PostConstruct
	public void bootstrapRegistration() {
		SimpleAttributes attributes = new SimpleAttributes();
		Map<String,Object> pairs = new HashMap<>();
		pairs.put("country", "USA");
		pairs.put("subscriptionLevel", "Prime");
		pairs.put("userLicense", "Private");
		pairs.put("age", 20);
		attributes.setAttributePairs(pairs);
		this.registerUser(attributes);
	}
	
	
	// ---------------- Initial interview controller ----------------

	@Override
	public ResponseEntity<SimpleJSONUuid> initializeServiceRequest(String name) {
		return initialInterviewController.initializeServiceRequest(name);
	}

	@Override
	public ResponseEntity<List<OTFProviderConfidence>> initialInterview(UUID requestUUID, String userInput) {
		return initialInterviewController.initialInterview(requestUUID, userInput);
	}
	
	// ---------------- OtfProvider controller     -------------------
	
	@Override
	public ResponseEntity<InterviewResponse> acceptOTFProvider(UUID requestUUID, UUID otfpUUID) {
		return otfProviderController.acceptOtfProvider(requestUUID, otfpUUID);
	}

	@Override
	public ResponseEntity<InterviewResponse> answerProsecoInterview(UUID requestUUID, Object interview) {
			return otfProviderController.answerProsecoInterview(requestUUID, interview);
	}
	
	// ---------------- Market controller          -------------------
	
	@Override
	public ResponseEntity<SimpleJSONMessage> registerUser(SimpleAttributes body) {
		return marketController.registerUser(body);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> buyOffer(UUID requestUUID, UUID offerUUID) {
		return marketController.buyOffer(requestUUID, offerUUID);
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> putRating(ServiceReputation body, UUID requestName) {
		return marketController.putRating(requestName, body);
	}

	// ---------------- Data management controller -------------------

	@Override
	public ResponseEntity<List<OfferAndRequest>> getAllOffers() {
		return dataController.getAllOffers();
	}

	@Override
	public ResponseEntity<List<Offer>> getOfferByCompositionID(UUID requestUUID) {
		return dataController.getAllOffersForRequest(requestUUID);
	}

	@Override
	public ResponseEntity<Offer> getSpecificOffer(UUID requestUUID, UUID offerUUID) {
		return dataController.getOfferForRequest(requestUUID, offerUUID);
	}

	@Override
	public ResponseEntity<List<ItemAndRequest>> getAllItems() {
		return dataController.getAllItems();
	}

	@Override
	public ResponseEntity<ItemAndRequest> getItemForRequest(UUID requestUUID) {
		return dataController.getItemForRequest(requestUUID);
	}

	@Override
	public ResponseEntity<List<JobstateAndRequest>> getAllJobStates() {
		return dataController.getAllJobStates();
	}

	@Override
	public ResponseEntity<ConfigurationMarketMonitorSources> getMonitorSourcesForRequest(UUID requestUUID) {
		return dataController.getMonitorSourcesForRequest(requestUUID);
	}

	@Override
	public ResponseEntity<Object> getExtractedInformation(UUID requestUUID) {
		return dataController.getExtractedInformationForRequest(requestUUID);
	}

	@Override
	public ResponseEntity<JobState> getJobStateForOTFProvider(UUID requestUUID) {
		return dataController.getJobstateForRequest(requestUUID);
	}
}
