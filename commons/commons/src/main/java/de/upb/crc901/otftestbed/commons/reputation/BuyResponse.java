package de.upb.crc901.otftestbed.commons.reputation;

import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.StandaloneRepresentable;
import de.upb.crypto.math.serialization.annotations.AnnotatedRepresentationUtil;
import de.upb.crypto.math.serialization.annotations.Represented;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssueResponse;

public class BuyResponse implements StandaloneRepresentable{
	@Represented
	private ReactReviewTokenIssueResponse reviewToken;
	
	@Represented
	private ReactCredentialIssueResponse accessToken;
	
	@Represented
	private String serviceLink;
	
	public BuyResponse(Representation repr) {
		AnnotatedRepresentationUtil.restoreAnnotatedRepresentation(repr, this);
	}
	
	public ReactReviewTokenIssueResponse getReviewToken() {
		return reviewToken;
	}

	public ReactCredentialIssueResponse getAccessToken() {
		return accessToken;
	}

	public String getServiceLink() {
		return serviceLink;
	}

	public BuyResponse(ReactReviewTokenIssueResponse reviewToken, ReactCredentialIssueResponse accessToken,
			String serviceLink) {
		super();
		this.reviewToken = reviewToken;
		this.accessToken = accessToken;
		this.serviceLink = serviceLink;
	}

	@Override
	public Representation getRepresentation() {
		return AnnotatedRepresentationUtil.putAnnotatedRepresentation(this);
	}

}
