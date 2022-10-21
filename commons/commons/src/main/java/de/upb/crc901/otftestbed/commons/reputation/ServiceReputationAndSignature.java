package de.upb.crc901.otftestbed.commons.reputation;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;

public class ServiceReputationAndSignature {

	@JsonProperty
	private ExtendedServiceReputation serviceReputation;
	
	@JsonProperty
	private ReactRepresentableReview signature;

	public ServiceReputationAndSignature(ExtendedServiceReputation serviceReputation,
			ReactRepresentableReview signature) {
		super();
		this.serviceReputation = serviceReputation;
		this.signature = signature;
	}
	
	public ServiceReputationAndSignature() {
		// json constructor
	}

	public ExtendedServiceReputation getServiceReputation() {
		return serviceReputation;
	}

	public ReactRepresentableReview getSignature() {
		return signature;
	}
	
	
}
