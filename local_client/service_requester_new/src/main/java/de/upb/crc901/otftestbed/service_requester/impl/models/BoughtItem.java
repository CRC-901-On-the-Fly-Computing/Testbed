package de.upb.crc901.otftestbed.service_requester.impl.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.ServiceState;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewToken;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;

public class BoughtItem {
	
	private WaitingForOfferData request;
	
	private String serviceLink;
	
	private Offer acceptedOffer;
	
	@JsonIgnore
	private ReactReviewToken reviewToken;
	
	@JsonIgnore
	private ReactNonInteractivePolicyProof accessCredential;
	
	private ServiceState serviceState;
	
	public BoughtItem() {
	}

	public WaitingForOfferData getRequest() {
		return request;
	}

	public void setRequest(WaitingForOfferData waitingForOfferData) {
		this.request = waitingForOfferData;
	}

	public String getServiceLink() {
		return serviceLink;
	}

	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}

	public ReactReviewToken getReviewToken() {
		return reviewToken;
	}

	public void setReviewToken(ReactReviewToken reviewToken) {
		this.reviewToken = reviewToken;
	}

	public ReactNonInteractivePolicyProof getAccessCredential() {
		return accessCredential;
	}

	public void setAccessCredential(ReactNonInteractivePolicyProof accessCredential) {
		this.accessCredential = accessCredential;
	}

	public Offer getAcceptedOffer() {
		return acceptedOffer;
	}

	public void setAcceptedOffer(Offer acceptedOffer) {
		this.acceptedOffer = acceptedOffer;
	}

	public ServiceState getServiceState() {
		return serviceState;
	}

	public void setServiceState(ServiceState serviceState) {
		this.serviceState = serviceState;
	}
}
