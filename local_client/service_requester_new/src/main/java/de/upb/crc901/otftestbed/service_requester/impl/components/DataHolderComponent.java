package de.upb.crc901.otftestbed.service_requester.impl.components;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.general.JobState;
import de.upb.crc901.otftestbed.commons.model.OTFProvider;
import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.commons.requester.*;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.MarketController;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoSuchOtfProviderException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoSuchRequestException;
import de.upb.crc901.otftestbed.service_requester.impl.models.*;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewToken;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.ReactUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

/**
 * The key component of the service requester. This component manages the
 * collected data for each request as well as maintains their respective request
 * state.
 * 
 * @author Mirko Jürgens
 *
 */
@Component
public class DataHolderComponent {

	private static final Logger log = LoggerFactory.getLogger(DataHolderComponent.class);

	@Autowired
	private PocConnector connect;

	@Autowired
	private MarketController marketcontroller;

	private boolean isRegistered = false;

	private ReactPublicParameters pp;

	// the user of the authentication system
	private ReactUser user;

	private Map<UUID, RequestState> interviewStateMap = new HashMap<>();

	// Convenience map for the user
	private Map<UUID, String> requestNameToRequestUUID = new HashMap<>();

	// all requests that are in progress of the interview -i.e. didn't chose an
	// otf-provider yet
	private Map<UUID, InitialInterviewData> initialInterviews = new HashMap<>();

	private Map<UUID, WaitingForOfferData> waitingForOfferRequests = new HashMap<>();

	// all requests that have finished the interview -i.e. selected an otf-provider
	// and are in progress of filling out the interview or are waiting for the
	// otf-provider to configure
	private Map<UUID, ProsecoInterviewData> prosecoInterviews = new HashMap<>();

	// all requests that are finished, i.e. the user bought it
	private Map<UUID, BoughtItem> boughtItems = new HashMap<>();

	/*
	 * Convenience method for checking is there is such an request and the
	 * registration was successful.
	 */
	public void checkForExistentRequestAndRegisteredUser(UUID requestUUID) {
		if (!isRegistered) {
			//throw new NoUserRegisteredException();

			//A workaround for the starting point when no user profile is present. This should be removed in future
			SimpleAttributes simpleAttributes = new SimpleAttributes();
			Map<String, Object> attributePairs = new HashMap<>();
			attributePairs.put("country", "USA");
			attributePairs.put("subscriptionLevel", "Prime");
			attributePairs.put("userLicense", "Private");
			attributePairs.put("age", 20);
			simpleAttributes.setAttributePairs(attributePairs);
			marketcontroller.registerUser(simpleAttributes);
		}
		/* We just check for registration here... */
		if (requestUUID == null) {
			return;
		}
		if (!interviewStateMap.containsKey(requestUUID)) {
			throw new NoSuchRequestException();
		}
		RequestState state = interviewStateMap.get(requestUUID);
		log.debug(state.toString());

		if (!requestNameToRequestUUID.containsKey(requestUUID)) {
			throw new NoSuchRequestException();
		}
		switch (state) {
		case INITIAL_INTERVIEW:
			if (!initialInterviews.containsKey(requestUUID)) {
				throw new NoSuchRequestException();
			}
			break;
		case PROSECO_INTERVIEW:
			if (!prosecoInterviews.containsKey(requestUUID)) {
				throw new NoSuchRequestException();
			}
			break;
		case WAITING_FOR_OFFERS:
			if (!waitingForOfferRequests.containsKey(requestUUID)) {
				throw new NoSuchRequestException();
			}
			break;
		case BOUGHT:
			if (!boughtItems.containsKey(requestUUID)) {
				throw new NoSuchRequestException();
			}
			break;
		default:
			break;
		}

	}

	// Methods used for the first phase of the interview (Selecting the name).

	/**
	 * Is called when the user starts a new request. Thus, it will create initial
	 * models in the system.
	 * 
	 * @param requestName the name of the user which does not need to be unique
	 * @param requestUUID the unique identifier of the request that is passed along
	 *                    with every service call
	 */
	public void initializeNewRequest(String requestName, UUID requestUUID) {
		interviewStateMap.put(requestUUID, RequestState.NAME_SELECTED);
		requestNameToRequestUUID.put(requestUUID, requestName);
	}

	// Methods used for the second phase of the interview (providing a natural
	// language description & processing by the chatbot)
	/**
	 * Is called when the user entered his natural languege description, chatbot was
	 * invoked to extract all data and the registry was queried for a list of
	 * possible providers
	 * 
	 * @param uuid                 the unique identifier
	 * @param userInput            the natural language
	 * @param extractedInformation the request information extracted by the chatbot
	 * @param registryConfidences  the list of possible otf-providers that can be
	 *                             used to continue this request.
	 */
	public void finishSecondStepOfInterview(UUID uuid, String userInput, DomainPreferences extractedInformation,
			List<OTFProviderConfidence> registryConfidences) {
		interviewStateMap.remove(uuid);
		interviewStateMap.put(uuid, RequestState.INITIAL_INTERVIEW);
		initialInterviews.put(uuid, new InitialInterviewData(extractedInformation, registryConfidences, userInput));
	}

	// Methods used for the third phase of the request (the user selects an
	// otf-provider and answers the interview)

	public void acceptOtfProvider(UUID requestUUID, UUID otfpUUID) {
		InitialInterviewData initialInterviewData = initialInterviews.get(requestUUID);
		List<OTFProviderConfidence> allOfferedProvidersConfidences = initialInterviewData.getOtfpConfidences();
		// check if there was such an otf-provider
		Optional<OTFProvider> optionalOtfProvider = allOfferedProvidersConfidences.stream()
				.filter(p -> p.getOtfProvider().getOtfpUUID().equals(otfpUUID))
				.map(OTFProviderConfidence::getOtfProvider).findFirst();

		OTFProvider otfProvider = optionalOtfProvider.orElseThrow(NoSuchOtfProviderException::new);

		// create the data model for this phase of the request.
		ProsecoInterviewData prosecoInterviewData = new ProsecoInterviewData(otfProvider, initialInterviewData);
		initialInterviews.remove(requestUUID);
		interviewStateMap.remove(requestUUID);
		interviewStateMap.put(requestUUID, RequestState.PROSECO_INTERVIEW);
		prosecoInterviews.put(requestUUID, prosecoInterviewData);
	}

	public ProsecoInterviewData getProsecoInterviewDataForUUID(UUID requestUUID) {
		return prosecoInterviews.get(requestUUID);
	}

	public void finishThirdStepOfInterview(UUID requestUUID) {
		ProsecoInterviewData prosecoInterviewData = prosecoInterviews.get(requestUUID);
		ConfigurationMarketMonitorSources sources = connect.toOtfProvider()
				.callProsecoConfigurator(prosecoInterviewData.getSelectedOTFProvider().getGatekeeperURL())
				.getSources(requestUUID);
		List<Offer> offers = new ArrayList<>();
		WaitingForOfferData waitingForOfferData = new WaitingForOfferData(prosecoInterviewData, offers, sources);
		interviewStateMap.remove(requestUUID);
		interviewStateMap.put(requestUUID, RequestState.WAITING_FOR_OFFERS);
		prosecoInterviews.remove(requestUUID);
		waitingForOfferRequests.put(requestUUID, waitingForOfferData);
	}

	// Methods used for the fourth phase of the request (user selects an offer)

	/**
	 * Retrieves a list of offer from the market-provider for this request.
	 * 
	 * @param requestUUID the unique identifier for this request
	 * @return the list of offers which have been submitted by the otf-provider to
	 *         the market-provider
	 */
	public List<Offer> getOffersForRequest(UUID requestUUID) {
		if (!waitingForOfferRequests.containsKey(requestUUID))
			return new ArrayList<>();
		/*
		 * As we are unsure whether or not our cached data is concise, we will retrieve
		 * it from the otf-provider
		 */
		WaitingForOfferData waitingForOfferData = waitingForOfferRequests.get(requestUUID);
		UUID otfpUUID = waitingForOfferData.getProsecoInterviewData().getSelectedOTFProvider().getOtfpUUID();
		List<Offer> offers = connect.toMarketProvider().callBuyProcessor().getOffersForRequest(otfpUUID, requestUUID);
		/* Cache the data */
		offers.forEach(o -> o
				.setCompositionID(o.getCompositionAndOTFProvider().getSimpleComposition().getServiceCompositionId()));
		waitingForOfferData.setOffers(offers);
		return offers;
	}

	/**
	 * Gets the information for a single offer.
	 * 
	 * @param requestUUID the unique identifier of the request
	 * @param offerUUID   the the unique identifier of the offer
	 * @return the details of the offer
	 */
	public Offer getOfferForRequest(UUID requestUUID, UUID offerUUID) {
		WaitingForOfferData waitingForOfferData = waitingForOfferRequests.get(requestUUID);
		UUID otfpUUID = waitingForOfferData.getProsecoInterviewData().getSelectedOTFProvider().getOtfpUUID();
		Offer offer = connect.toMarketProvider().callBuyProcessor().getOffer(otfpUUID, requestUUID, offerUUID);
		offer.setCompositionID(offer.getCompositionAndOTFProvider().getSimpleComposition().getServiceCompositionId());
		return offer;
	}

	/**
	 * Finishes phase four of a request. That is, the controller has successfully
	 * bought the item from the market-provider.
	 * 
	 * @param requestUUID the unique identifier
	 * @param offer       the offer which has been bought
	 * @param serviceLink the url where the bought service can be found
	 */
	public void finishPhaseFourOfRequest(UUID requestUUID, Offer offer, String serviceLink) {
		WaitingForOfferData prosecoInterviewData = waitingForOfferRequests.get(requestUUID);
		BoughtItem item = new BoughtItem();
		item.setRequest(prosecoInterviewData);
		item.setAcceptedOffer(offer);
		item.setServiceLink(serviceLink);
		item.setServiceState(ServiceState.BOOTING);
		boughtItems.put(requestUUID, item);
		waitingForOfferRequests.remove(requestUUID);
		interviewStateMap.put(requestUUID, RequestState.BOUGHT);
	}

	public void setCredentialForUUID(UUID requestUUID, ReactNonInteractivePolicyProof credential) {
		BoughtItem item = boughtItems.get(requestUUID);
		item.setAccessCredential(credential);
	}

	public void setReviewTokenForUUID(UUID requestUUID, ReactReviewToken token) {
		BoughtItem item = boughtItems.get(requestUUID);
		item.setReviewToken(token);
	}

	// Methods used for rating the service
	public ReactReviewToken getReviewTokenForUUID(UUID requestUUID) {
		BoughtItem item = boughtItems.get(requestUUID);
		return item.getReviewToken();
	}

	// Methods used for the ui -> collecting data etc

	public JobState getJobStateForRequest(UUID requestUUID) {
		String prosecoUrl = getOtfProviderURLForRequest(requestUUID);
		return connect.toOtfProvider().callProsecoConfigurator(prosecoUrl).getJobstate(requestUUID);
	}

	public Map<String, String> getExtractedInformation(UUID request) {
		if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.PROSECO_INTERVIEW)) {
			ProsecoInterviewData prosecoInterviewData = prosecoInterviews.get(request);
			return prosecoInterviewData.getProsecoValues();
		} else if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.WAITING_FOR_OFFERS)) {
			WaitingForOfferData waitingForOfferData = waitingForOfferRequests.get(request);
			ProsecoInterviewData prosecoInterviewData = waitingForOfferData.getProsecoInterviewData();
			return prosecoInterviewData.getProsecoValues();

		} else if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.BOUGHT)) {
			BoughtItem boughtItem = boughtItems.get(request);
			WaitingForOfferData waitingForOfferData = boughtItem.getRequest();
			ProsecoInterviewData prosecoInterviewData = waitingForOfferData.getProsecoInterviewData();
			return prosecoInterviewData.getProsecoValues();

		} else {
			throw new NoSuchRequestException();
		}
	}

	/**
	 * Provides a list of all ongoing offers of the OTF-Provider
	 * 
	 * @return
	 */
	public List<OfferAndRequest> getAllOffers() {
		List<OfferAndRequest> offerAndRequest = new ArrayList<>();
		for (Map.Entry<UUID, WaitingForOfferData> pendingRequests : waitingForOfferRequests.entrySet()) {
			UUID requestUUID = pendingRequests.getKey();
			List<Offer> offersForRequest = getOffersForRequest(requestUUID);
			if (offersForRequest != null && !offersForRequest.isEmpty()) {
				offerAndRequest.add(
						new OfferAndRequest(requestNameToRequestUUID.get(requestUUID), requestUUID, offersForRequest));
			}
		}
		return offerAndRequest;
	}

	/**
	 * Provides a list of all items that were bought by this user along with their
	 * request uuid and name.
	 * 
	 * @return
	 */
	public List<ItemAndRequest> getAllItems() {
		List<ItemAndRequest> itemAndRequests = new ArrayList<>();
		for (Map.Entry<UUID, BoughtItem> boughtItem : boughtItems.entrySet()) {
			UUID requestUUID = boughtItem.getKey();
			BoughtItem item = boughtItem.getValue();
			itemAndRequests.add(
					new ItemAndRequest(requestNameToRequestUUID.get(requestUUID), requestUUID, item.getServiceLink(),
							item.getAcceptedOffer(), item.getServiceState(), item.getAccessCredential()));
		}
		return itemAndRequests;
	}

	/**
	 * Looks up a specific item that was bought by this user.
	 * 
	 * @param requestUUID
	 * @return
	 */
	public ItemAndRequest getItemForRequest(UUID requestUUID) {
		BoughtItem item = boughtItems.get(requestUUID);
		return new ItemAndRequest(requestNameToRequestUUID.get(requestUUID), requestUUID, item.getServiceLink(),
				item.getAcceptedOffer(), item.getServiceState(), item.getAccessCredential());
	}

	/**
	 * Looks up the offer that is associated with the item.
	 * 
	 * @param requestName
	 * @return
	 */
	public Offer getBoughtOfferForRequest(UUID requestName) {
		BoughtItem item = boughtItems.get(requestName);
		return item.getAcceptedOffer();
	}

	/**
	 * Provides a list of all ongoing requests along with their request uuid and
	 * name.
	 * 
	 * @return
	 */
	public List<JobstateAndRequest> getAllJobstates() {
		List<JobstateAndRequest> requests = new ArrayList<>();
		for (Entry<UUID, WaitingForOfferData> request : waitingForOfferRequests.entrySet()) {
			try {
				log.debug("Request : {} with state {} ", request.getKey(), interviewStateMap.get(request.getKey()));
				requests.add(new JobstateAndRequest(requestNameToRequestUUID.get(request.getKey()), request.getKey(),
						getJobStateForRequest(request.getKey())));
			} catch (Exception e) {
				log.warn("UUID : {} has no jobstate!", request.getKey());
			}
		}
		return requests;
	}

	public BoughtItem getBoughtItemForRequest(UUID requestUUID) {
		return boughtItems.get(requestUUID);

	}

	// Methods used by the C3 authentication system

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public ReactPublicParameters getPp() {
		return pp;
	}

	public void setPp(ReactPublicParameters pp) {
		this.pp = pp;
	}

	public ReactUser getUser() {
		return user;
	}

	public void setUser(ReactUser user) {
		this.user = user;
	}

	// Helper methods
	public String getOtfProviderURLForRequest(UUID request) {

		if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.PROSECO_INTERVIEW)) {
			ProsecoInterviewData prosecoInterviewData = prosecoInterviews.get(request);
			OTFProvider provider = prosecoInterviewData.getSelectedOTFProvider();
			return provider.getGatekeeperURL();
		} else if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.WAITING_FOR_OFFERS)) {
			WaitingForOfferData waitingForOfferData = waitingForOfferRequests.get(request);
			ProsecoInterviewData prosecoInterviewData = waitingForOfferData.getProsecoInterviewData();
			OTFProvider provider = prosecoInterviewData.getSelectedOTFProvider();
			return provider.getGatekeeperURL();
		} else if (interviewStateMap.containsKey(request)
				&& interviewStateMap.get(request).equals(RequestState.BOUGHT)) {
			BoughtItem boughtItem = boughtItems.get(request);
			WaitingForOfferData waitingForOfferData = boughtItem.getRequest();
			ProsecoInterviewData prosecoInterviewData = waitingForOfferData.getProsecoInterviewData();
			OTFProvider provider = prosecoInterviewData.getSelectedOTFProvider();
			return provider.getGatekeeperURL();
		} else {
			throw new NoSuchRequestException();
		}
	}

	public ConfigurationMarketMonitorSources getConfigurationGraphFOrRequest(UUID requestUUID) {
		WaitingForOfferData waitingForOfferData = waitingForOfferRequests.get(requestUUID);
		return waitingForOfferData.getConfigurationMonitorSources();
	}

	public WaitingForOfferData getWaitingForOfferData(UUID requestUUID) {
		return waitingForOfferRequests.get(requestUUID);
	}

}
