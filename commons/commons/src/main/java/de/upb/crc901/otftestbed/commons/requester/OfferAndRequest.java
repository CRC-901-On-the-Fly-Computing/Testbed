package de.upb.crc901.otftestbed.commons.requester;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferAndRequest {

	@JsonProperty
	private final String requestName;

	@JsonProperty
	private final UUID requestUUID;

	@JsonProperty
	private final List<Offer> offers;

	public OfferAndRequest(String reuqestName, UUID requestUUID, List<Offer> offers) {
		this.requestName = reuqestName;
		this.offers = offers;
		this.requestUUID = requestUUID;
	}
}
