package de.upb.crc901.otftestbed.commons.requester;

public class OfferAndImage {

	private Offer offer;

	private String dockerImageURL;
	
	public OfferAndImage() {}

	public OfferAndImage(Offer offer, String dockerImageURL) {
		super();
		this.offer = offer;
		this.dockerImageURL = dockerImageURL;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public String getDockerImageURL() {
		return dockerImageURL;
	}

	public void setDockerImageURL(String dockerImageURL) {
		this.dockerImageURL = dockerImageURL;
	}

}
