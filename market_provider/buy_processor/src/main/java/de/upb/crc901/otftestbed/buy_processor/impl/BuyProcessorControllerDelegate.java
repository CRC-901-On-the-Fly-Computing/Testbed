package de.upb.crc901.otftestbed.buy_processor.impl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.buy_processor.generated.spring_server.api.BuyProcessorApiDelegate;
import de.upb.crc901.otftestbed.buy_processor.impl.exceptions.AuthenticationFailedException;
import de.upb.crc901.otftestbed.buy_processor.impl.models.StoredOffer;
import de.upb.crc901.otftestbed.buy_processor.impl.repositories.OfferRepository;
import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.reputation.BuyResponse;
import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.OfferAndImage;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONUuid;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuerPublicIdentity;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.verifier.credentials.VerificationResult;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

@Component
public class BuyProcessorControllerDelegate implements BuyProcessorApiDelegate {

	private static final Logger log = LoggerFactory.getLogger(BuyProcessorControllerDelegate.class);

	@Autowired
	private PocConnector connect;

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private ReviewTokenIssuerComponent reviewTokenIssuerComponent;

	@Autowired
	private AuthChecker authChecker;

	@Autowired
	private AccessTokenIssuerComponent accessIssuer;

	@Autowired
	private DeploymentComponent deploymentComponent;

	@Autowired
	private OfferRepository offerRepository;

	private boolean isInitialized;

	private ReactPublicParameters pp;

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

	public ResponseEntity<SimpleJSONMessage> initialize() {
		if (!isInitialized) {
			log.debug("Requesting the public parameters from the system manager...");
			pp = connect.toMarketProvider().callSystemManager().getPublicParameter();
			log.debug("Initializing the Review token issuer...");
			reviewTokenIssuerComponent.initialize(pp);
			log.debug("Initializing the auth checker ...");
			authChecker.initialize(pp);
			log.debug("Initializing the access token issuer...");
			accessIssuer.initialize(pp);
			isInitialized = true;
			return new ResponseEntity<>(new SimpleJSONMessage("Initialization successful"), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new SimpleJSONMessage("Already initialized"), HttpStatus.CREATED);
		}
	}

	public ResponseEntity<SimpleJSONMessage> deinitialize() {
		if (isInitialized) {
			log.debug("Deinitializing ...");
			reviewTokenIssuerComponent.deinitialize();
			authChecker.deinitialize();
			pp = null;
			isInitialized = false;
			return new ResponseEntity<>(new SimpleJSONMessage("Deinitialization successful"), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new SimpleJSONMessage("Already deinitialized"), HttpStatus.CREATED);
		}
	}

	@Override
	public ResponseEntity<Offer> getOffer(UUID otfpUUID, UUID requestUUID, UUID offerUUID) {
		if (!isInitialized) {
			initialize();
		}
		log.debug("Got get request for offer {} for the otfp {} under the global request {}", offerUUID, otfpUUID,
				requestUUID);
		StoredOffer storedOffer = offerRepository.getOffer(requestUUID, otfpUUID, offerUUID);
		log.debug("Returning offer...");
		return ResponseEntity.ok(storedOffer.getOffer());
	}

	@Override
	public ResponseEntity<BuyResponse> buyItem(BuyTokens body, UUID otfpUUID, UUID requestUUID, UUID offerUUID) {
		if (!isInitialized) {
			initialize();
		}
		log.debug("Got buy request for offer {} for the otfp {} under the global request {}", offerUUID, otfpUUID,
				requestUUID);
		log.debug("Retrieving offer from the database...");

		StoredOffer storedOffer = offerRepository.getOffer(requestUUID, otfpUUID, offerUUID);
		String serviceCompositionIdentifier = storedOffer.getOffer().getCompositionID();
		log.debug("A user requested the purchase of item {}", serviceCompositionIdentifier);
		log.debug("A user requested the purchase of services {}",
				storedOffer.getOffer().getCompositionAndOTFProvider().getSimpleComposition());
		log.debug("Checking whether the user is authenticated");
		VerificationResult result = authChecker.validateAuthenticationToken(offerUUID.toString(),
				body.getAuthenticationToken(), storedOffer.getOffer().getPolicyInformation());
		log.debug("The authentication yielded {}", result.isVerify());
//		telemetry.logAuthenticationBuy(result.isVerify(), storedOffer.getOffer().getSimplePolicy());

		if (!result.isVerify()) {
			log.error("User authentication failed...");
			throw new AuthenticationFailedException();
		}

		// checking hash of item
		byte[] hashOfRequestedItem = body.getReviewTokenRequest().getIssuable().getItem().getData().getData();

		Offer offeredItem = storedOffer.getOffer();
		PolicyInformation pi = offeredItem.getPolicyInformation();
		offeredItem.setPolicyInformation(null);
		log.debug("Offer is {}", offeredItem);

		byte[] itemBytes;
		try {
			String itemAsJson = Helpers.getMapper().writeValueAsString(offeredItem);
			itemAsJson = itemAsJson.trim();
			log.debug("Length of item as json is {}", itemAsJson.length());
			itemBytes = itemAsJson.getBytes(StandardCharsets.UTF_8);
			log.debug("Length of item as bytes is {}", itemBytes.length);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}

		byte[] hashOfOfferedItem = pp.getHashIntoZp().hashIntoStructure(itemBytes).getUniqueByteRepresentation();
		offeredItem.setPolicyInformation(pi);
		if (log.isDebugEnabled()) {
			log.debug("Hash is {}", Arrays.toString(hashOfOfferedItem));
		}

		if (!Arrays.equals(hashOfRequestedItem, hashOfOfferedItem)) {
			log.error(
					"The hash of the offer in the request does not match the hash of the offer computed by the buy-processor.");
			throw new AuthenticationFailedException();
		}
		String hashOfOffer = new Base64().encodeAsString(hashOfOfferedItem);
		log.debug("Authentication successful, creating ReviewToken...");
		ReactReviewTokenIssueResponse reviewToken = reviewTokenIssuerComponent.getReviewTokenIssuer(pp)
				.issueNonInteractively(body.getReviewTokenRequest());

		log.debug("Deploying bought service...");
		String serviceLink = deploymentComponent.deployDockerImage(storedOffer.getDockerImageURL(), hashOfOffer);

//		telemetry.logTransaction(storedOffer.getOffer());

		log.debug("Creating access token...");
		ReactCredentialIssueResponse accessToken = accessIssuer.requestCredential(body.getAccessTokenRequest());
		log.debug("Returning...");

		return ResponseEntity.ok(new BuyResponse(reviewToken, accessToken, serviceLink));
	}

	@Override
	public ResponseEntity<SimpleJSONUuid> enterOffer(OfferAndImage offer, UUID otfpUUID, UUID requestUUID) {
		log.debug("Received an offer storage request from {} for request {} ", otfpUUID, requestUUID);

		UUID offerUUID = UUID.randomUUID();
		offer.getOffer().setOfferUUID(offerUUID);
		StoredOffer storedOffer = new StoredOffer(offerUUID, otfpUUID, requestUUID, offer.getOffer(),
				offer.getDockerImageURL());
		offerRepository.insert(storedOffer);
		log.debug("Inserted the offer!");

		return ResponseEntity.ok(new SimpleJSONUuid(offerUUID));
	}

	@Override
	public ResponseEntity<List<Offer>> getOffersForRequest(UUID otfpUUID, UUID requestUUID) {
		log.debug("Collecting offers...");

		List<StoredOffer> offers = offerRepository.getOffersByRequestUUID(requestUUID, otfpUUID);
		List<Offer> mappedOffers = offers.stream().map(StoredOffer::getOffer).collect(Collectors.toList());

		return ResponseEntity.ok(mappedOffers);
	}

	@Override
	public ResponseEntity<ReactCredentialIssuerPublicIdentity> getAccessTokenIssuerPublicIdentity() {
		if (!isInitialized) {
			initialize();
			// throw new BuyProcessorNotInitializedException();
		}
		return ResponseEntity.ok(accessIssuer.getIssuer().getPublicIdentity());
	}

	@Override
	public ResponseEntity<ReactReviewTokenIssuerPublicIdentity> getReviewTokenIssuerPublicIdentity() {
		if (!isInitialized) {
			initialize();
			// throw new BuyProcessorNotInitializedException();
		}
		return ResponseEntity.ok(reviewTokenIssuerComponent.getReviewTokenIssuer(pp).getPublicIdentity());
	}

	@Override
	public ResponseEntity<ReactVerifierPublicIdentity> getPublicIdentity() {
		if (!isInitialized) {
			initialize();
			// throw new BuyProcessorNotInitializedException();
		}
		return ResponseEntity.ok(authChecker.getPublicIdentity());
	}
}
