package de.upb.crc901.otftestbed.commons.requester;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;

public class ItemAndRequest {

	@JsonProperty
	private final String requestName;

	@JsonProperty
	private final String serviceLink;
	
	@JsonProperty
	private final UUID requestUUID;

	@JsonProperty
	private final Offer acceptedOffer;
	
	@JsonProperty
	private final ServiceState serviceState;
	
	@JsonProperty
	private final ReactNonInteractivePolicyProof accessCredential; 

	public ItemAndRequest(String reuqestName, UUID requestUUID, String serviceLink, Offer acceptedOffer, ServiceState state, ReactNonInteractivePolicyProof accessCredential) {
		this.requestName = reuqestName;
		this.acceptedOffer = acceptedOffer;
		this.requestUUID = requestUUID;
		this.serviceLink = serviceLink;
		this.accessCredential = accessCredential;
		this.serviceState = state;
	}

	public String getRequestName() {
		return requestName;
	}

	public String getServiceLink() {
		return serviceLink;
	}

	public UUID getRequestUUID() {
		return requestUUID;
	}

	public Offer getAcceptedOffer() {
		return acceptedOffer;
	}

	public ServiceState getServiceState() {
		return serviceState;
	}

	public ReactNonInteractivePolicyProof getAccessCredential() {
		return accessCredential;
	}
}
