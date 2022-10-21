package de.upb.crc901.otftestbed.commons.reputation;

import de.upb.crypto.math.serialization.Representable;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.annotations.AnnotatedRepresentationUtil;
import de.upb.crypto.math.serialization.annotations.Represented;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import de.upb.crypto.react.acs.user.impl.react.reviewtoken.ReactNonInteractiveReviewTokenRequest;

public class BuyTokens implements Representable{
	
	@Represented
	private ReactNonInteractivePolicyProof authenticationToken;
	
	@Represented
	private ReactNonInteractiveReviewTokenRequest reviewTokenRequest;
	
	@Represented
	private ReactNonInteractiveCredentialRequest accessTokenRequest;
	
	public BuyTokens(Representation repr) {
		AnnotatedRepresentationUtil.restoreAnnotatedRepresentation(repr, this);
	}
	
	
	public BuyTokens(ReactNonInteractivePolicyProof nonInteractivePolicyProof, ReactNonInteractiveReviewTokenRequest reactNonInteractivePolicyProof, ReactNonInteractiveCredentialRequest accessTokenReuqest) {
		this.authenticationToken = nonInteractivePolicyProof;
		this.reviewTokenRequest = reactNonInteractivePolicyProof;
		this.accessTokenRequest = accessTokenReuqest;
	}

	public ReactNonInteractivePolicyProof getAuthenticationToken() {
		return authenticationToken;
	}

	public ReactNonInteractiveCredentialRequest getAccessTokenRequest() {
		return accessTokenRequest;
	}

	public ReactNonInteractiveReviewTokenRequest getReviewTokenRequest() {
		return reviewTokenRequest;
	}


	public Representation getRepresentation() {
		return AnnotatedRepresentationUtil.putAnnotatedRepresentation(this);
	}
}
