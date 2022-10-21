package de.upb.crc901.otftestbed.service_requester.impl.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.crc901.otftestbed.commons.model.OTFProvider;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse;

public class ProsecoInterviewData {

	/* The OTF-Provider that was selected at the start of this process. */
	private OTFProvider selectedOTFProvider;

	/* A String, String map of answered questions. */
	private Map<String, String> prosecoValues = new HashMap<>();

	/*
	 * A list of collected questions, used for checking if a response fits the
	 * question.
	 */
	private List<InterviewResponse> collectedProsecoQuestions = new ArrayList<>();

	/* A reference to all the data of the previous interview step. */
	private InitialInterviewData initialInterviewData;

	public ProsecoInterviewData(OTFProvider selectedOtfProvider, InitialInterviewData initialInterviewData) {
		super();
		this.selectedOTFProvider = selectedOtfProvider;
		this.initialInterviewData = initialInterviewData;
		this.prosecoValues = initialInterviewData.getDomainPreferences().getKnownFacts();
		this.prosecoValues.put("domain", initialInterviewData.getDomainPreferences().getDomain().toLowerCase());
	}

	public OTFProvider getSelectedOTFProvider() {
		return selectedOTFProvider;
	}

	public Map<String, String> getProsecoValues() {
		return prosecoValues;
	}

	public void addProsecoQuestion(InterviewResponse response) {
		collectedProsecoQuestions.add(response);
	}

	public void updateProsecoValues(Map<String, String> newInformation) {
		for (Map.Entry<String, String> entry : newInformation.entrySet()) {
			this.prosecoValues.put(entry.getKey(), entry.getValue());
		}
	}

	public InitialInterviewData getInitialInterviewData() {
		return initialInterviewData;
	}

	public List<InterviewResponse> getAllQuestions() {
		return collectedProsecoQuestions;
	}

}
