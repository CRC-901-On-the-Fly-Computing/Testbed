package de.upb.crc901.otftestbed.service_requester.impl.models;

import java.util.List;

import de.upb.crc901.otftestbed.commons.requester.DomainPreferences;
import de.upb.crc901.otftestbed.commons.requester.OTFProviderConfidence;

/**
 * The data model for the first phase of the request. This data encapsulates all
 * information between the user and the chatbot. We went full google and save
 * all the data the user ever enters.
 * 
 * @author Mirko Jürgens
 *
 */
public class InitialInterviewData {

	/* The response of the chatbot to the user input. */
	private DomainPreferences domainPreferences;

	/* The natural language request of the user. */
	private String userRequest;

	/* The offered otf-providers */
	private List<OTFProviderConfidence> otfpConfidences;

	public InitialInterviewData(DomainPreferences domainPreferences,
			List<OTFProviderConfidence> otfProviderConfidences, String naturalLanguageInput) {
		super();
		this.otfpConfidences = otfProviderConfidences;
		this.domainPreferences = domainPreferences;
		this.userRequest = naturalLanguageInput;
	}


	public DomainPreferences getDomainPreferences() {
		return domainPreferences;
	}

	public List<OTFProviderConfidence> getOtfpConfidences() {
		return otfpConfidences;
	}

	public String getUserRequest() {
		return userRequest;
	}

}
