package de.upb.crc901.otftestbed.service_requester.impl.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.connect.client.ChatbotClient;
import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.requester.DomainPreferences;
import de.upb.crc901.otftestbed.commons.requester.OTFProviderConfidence;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONUuid;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;

@Component
public class InitialInterviewController {

	private static final Logger logger = LoggerFactory.getLogger(InitialInterviewController.class);

	@Autowired
	private PocConnector connect;

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private DataHolderComponent holder;

	@Autowired
	private ChatbotClient chatbotClient;


	/* First step of the interview: the user selects a name. */
	public ResponseEntity<SimpleJSONUuid> initializeServiceRequest(String name) {
		holder.checkForExistentRequestAndRegisteredUser(null);
		UUID uuid = UUID.randomUUID();
		holder.initializeNewRequest(name, uuid);
		logger.debug("Step 1 for request {} invoked. Name is {}", uuid, name);
		return ResponseEntity.ok(new SimpleJSONUuid(uuid));
	}

	/*
	 * Second step of the interview: the user provides a natural language
	 * description of the request.
	 */
	public ResponseEntity<List<OTFProviderConfidence>> initialInterview(UUID requestUUID, String userInput) {
		// check if this request is valid.
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);

		logger.debug("Step 2 for request {} invoked. Sending the user input to the chatbot to extract information.",
				requestUUID);

		logger.debug("Invoking chatbot.");
		// call the chatbot
		DomainPreferences domainPreferences = chatbotClient.postText(userInput);

//		telemetry.logNlpRequest(domainPreferences.getPerformanceMetrics());

		Map<String, String> extractedRequestInformation = domainPreferences.getKnownFacts();
		logger.debug("Chatbot returned: {}", extractedRequestInformation);

		// now call the registry to get a list of possible otf providers

		String domain = domainPreferences.getDomain().toLowerCase();
		List<OTFProviderConfidence> possibleOtfProviders = connect.toMarketProvider().callOtfpRegistry()
				.getConfidencesForProviders(extractedRequestInformation, domain);

		logger.debug("Possible otf-providers for this request are: {}", possibleOtfProviders);

		// If there are no entries, we require the user to reformulate his description.
		if (possibleOtfProviders.isEmpty()) {
			return ResponseEntity.ok().build();
		}

		holder.finishSecondStepOfInterview(requestUUID, userInput, domainPreferences, possibleOtfProviders);

		return ResponseEntity.ok(possibleOtfProviders);
	}
}
