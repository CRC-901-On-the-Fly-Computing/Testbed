package de.upb.crc901.otftestbed.service_requester.impl.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.general.JobState;
import de.upb.crc901.otftestbed.commons.requester.ConfigurationMarketMonitorSources;
import de.upb.crc901.otftestbed.commons.requester.ItemAndRequest;
import de.upb.crc901.otftestbed.commons.requester.JobstateAndRequest;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.OfferAndRequest;
import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;

/**
 * Answers all requests that are data management related, such as simple getters
 * etc.
 * 
 * @author Mirko Jürgens
 *
 */
@Component
public class DataManagementController {

	@Autowired
	private DataHolderComponent holder;

	// ---- Jobstates ---

	public ResponseEntity<List<JobstateAndRequest>> getAllJobStates() {
		return ResponseEntity.ok(holder.getAllJobstates());
	}

	public ResponseEntity<JobState> getJobstateForRequest(UUID requestUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		return ResponseEntity.ok(holder.getJobStateForRequest(requestUUID));
	}

	// --- Market Monitor Stuff ---

	public ResponseEntity<ConfigurationMarketMonitorSources> getMonitorSourcesForRequest(UUID requestUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		return ResponseEntity.ok(holder.getConfigurationGraphFOrRequest(requestUUID));
	}

	// --- Items ---

	public ResponseEntity<List<ItemAndRequest>> getAllItems() {
		return ResponseEntity.ok(holder.getAllItems());
	}

	public ResponseEntity<ItemAndRequest> getItemForRequest(UUID requestUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);

		return ResponseEntity.ok(holder.getItemForRequest(requestUUID));
	}

	// --- Offers ---

	public ResponseEntity<List<OfferAndRequest>> getAllOffers() {
		return ResponseEntity.ok(holder.getAllOffers());
	}

	public ResponseEntity<List<Offer>> getAllOffersForRequest(UUID requestUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		return ResponseEntity.ok(holder.getOffersForRequest(requestUUID));
	}

	public ResponseEntity<Offer> getOfferForRequest(UUID requestUUID, UUID offerUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		return ResponseEntity.ok(holder.getOfferForRequest(requestUUID, offerUUID));
	}

	// --- Interview stuff ---

	public ResponseEntity<Object> getExtractedInformationForRequest(UUID requestUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);
		return ResponseEntity.ok(holder.getExtractedInformation(requestUUID));
	}

}
