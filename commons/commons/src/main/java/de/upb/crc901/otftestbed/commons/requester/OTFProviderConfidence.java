package de.upb.crc901.otftestbed.commons.requester;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.otftestbed.commons.model.OTFProvider;

public class OTFProviderConfidence {

	@JsonProperty
	private OTFProvider otfProvider;
	
    @JsonProperty
    private double confidence;

    public OTFProviderConfidence() {
    }

	public OTFProvider getOtfProvider() {
		return otfProvider;
	}

	public double getConfidence() {
		return confidence;
	}

	public OTFProviderConfidence(OTFProvider otfProvider, double confidence) {
		super();
		this.otfProvider = otfProvider;
		this.confidence = confidence;
	}

	@Override
	public String toString() {
		return "OTFProviderConfidence [otfProvider=" + otfProvider + ", confidence=" + confidence + "]";
	}

}
