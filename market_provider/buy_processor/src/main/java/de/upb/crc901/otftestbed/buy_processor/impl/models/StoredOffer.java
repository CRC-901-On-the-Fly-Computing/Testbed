package de.upb.crc901.otftestbed.buy_processor.impl.models;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import de.upb.crc901.otftestbed.commons.requester.Offer;

@Document(collection = "offers")
public class StoredOffer {

	private UUID offerUUID;
	
	private UUID otfpUUID;
	
	private UUID requestUUID;
	
	private Offer offer;
	
	private String dockerImageURL;

	public StoredOffer() {}
	
	public StoredOffer(UUID offerUUID, UUID otfpUUID, UUID requestUUID, Offer offer, String dockerImageURL) {
		super();
		this.offerUUID = offerUUID;
		this.otfpUUID = otfpUUID;
		this.requestUUID = requestUUID;
		this.offer = offer;
		this.dockerImageURL = dockerImageURL;
	}
	
	
	public UUID getOfferUUID() {
		return offerUUID;
	}

	public UUID getOtfpUUID() {
		return otfpUUID;
	}

	public UUID getRequestUUID() {
		return requestUUID;
	}

	public Offer getOffer() {
		return offer;
	}

	public String getDockerImageURL() {
		return dockerImageURL;
	}

	public void setOfferUUID(UUID offerUUID) {
		this.offerUUID = offerUUID;
	}

	public void setOtfpUUID(UUID otfpUUID) {
		this.otfpUUID = otfpUUID;
	}

	public void setRequestUUID(UUID requestUUID) {
		this.requestUUID = requestUUID;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public void setDockerImageURL(String dockerImageURL) {
		this.dockerImageURL = dockerImageURL;
	}
	
}
