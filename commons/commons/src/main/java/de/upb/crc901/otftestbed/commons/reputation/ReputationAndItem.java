package de.upb.crc901.otftestbed.commons.reputation;

import de.upb.crc901.otftestbed.commons.requester.Offer;

public class ReputationAndItem {

	ServiceReputation reputation;

	Offer offer;

	public ReputationAndItem(ServiceReputation reputation, Offer offer) {
		super();
		this.reputation = reputation;
		this.offer = offer;
	}

	public ReputationAndItem() {
	}

	public ServiceReputation getReputation() {
		return reputation;
	}

	public void setReputation(ServiceReputation reputation) {
		this.reputation = reputation;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

}
