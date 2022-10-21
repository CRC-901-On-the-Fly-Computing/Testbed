package de.upb.crc901.otftestbed.commons.as.mappers;

import com.fasterxml.jackson.databind.module.SimpleModule;

import de.upb.crc901.otftestbed.commons.as.mappers.impl.BuyResponseDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.BuyResponseSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.BuyTokensDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.BuyTokensSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.PolicyInformationDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.PolicyInformationSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactCredentialIssueResponseDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactCredentialIssueResponseSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactCredentialIssuerPublicIdentityDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactCredentialIssuerPublicIdentitySerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactJoinResponseDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactJoinResponseSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactNonInteractiveCredentialRequestDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactNonInteractiveCredentialRequestSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactNonInteractiveJoinRequestDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactNonInteractiveJoinRequestSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactNonInteractivePolicyProofSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactPublicParametersDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactPublicParametersSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactRepresentableReviewDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactRepresentableReviewSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewTokenIssueResponseDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewTokenIssueResponseSerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewTokenIssuerPublicIdentityDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactReviewTokenIssuerPublicIdentitySerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactSystemManagerPublicIdentitySerializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactVerifierPublicIdentitySerializer;
import de.upb.crc901.otftestbed.commons.reputation.BuyResponse;
import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuerPublicIdentity;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;
import de.upb.crypto.react.acs.review.impl.react.ReactReview;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactJoinResponse;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractiveJoinRequest;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

public class SimpleACSModule {

	public static SimpleModule getModule() {
		SimpleModule simpleModule = new SimpleModule();

		simpleModule.addSerializer(ReactCredentialIssueResponse.class, new ReactCredentialIssueResponseSerializer());
		simpleModule.addDeserializer(ReactCredentialIssueResponse.class,
				new ReactCredentialIssueResponseDeserializer());
		simpleModule.addSerializer(ReactNonInteractiveCredentialRequest.class,
				new ReactNonInteractiveCredentialRequestSerializer());
		simpleModule.addDeserializer(ReactNonInteractiveCredentialRequest.class,
				new ReactNonInteractiveCredentialRequestDeserializer());
		simpleModule.addSerializer(ReactCredentialIssuerPublicIdentity.class,
				new ReactCredentialIssuerPublicIdentitySerializer());
		simpleModule.addDeserializer(ReactCredentialIssuerPublicIdentity.class,
				new ReactCredentialIssuerPublicIdentityDeserializer());
		simpleModule.addSerializer(ReactPublicParameters.class, new ReactPublicParametersSerializer());
		simpleModule.addDeserializer(ReactPublicParameters.class, new ReactPublicParametersDeserializer());
		simpleModule.addSerializer(ReactJoinResponse.class, new ReactJoinResponseSerializer());
		simpleModule.addDeserializer(ReactJoinResponse.class, new ReactJoinResponseDeserializer());
		simpleModule.addSerializer(ReactSystemManagerPublicIdentity.class,
				new ReactSystemManagerPublicIdentitySerializer());
		// simpleModule.addDeserializer(ReactSystemManagerPublicIdentity.class, new
		// ReactSystemManagerPublicIdentityDeserializer());
		simpleModule.addSerializer(ReactNonInteractiveJoinRequest.class,
				new ReactNonInteractiveJoinRequestSerializer());
		simpleModule.addDeserializer(ReactNonInteractiveJoinRequest.class,
				new ReactNonInteractiveJoinRequestDeserializer());

		simpleModule.addSerializer(BuyResponse.class, new BuyResponseSerializer());
		simpleModule.addDeserializer(BuyResponse.class, new BuyResponseDeserializer());

		simpleModule.addSerializer(BuyTokens.class, new BuyTokensSerializer());
		simpleModule.addDeserializer(BuyTokens.class, new BuyTokensDeserializer());
		simpleModule.addSerializer(ReactReviewTokenIssueResponse.class, new ReactReviewTokenIssueResponseSerializer());
		simpleModule.addDeserializer(ReactReviewTokenIssueResponse.class,
				new ReactReviewTokenIssueResponseDeserializer());
		simpleModule.addSerializer(ReactVerifierPublicIdentity.class, new ReactVerifierPublicIdentitySerializer());
		// simpleModule.addDeserializer(ReactVerifierPublicIdentity.class, new
		// ReactVerifierPublicIdentityDeserializer());
		simpleModule.addSerializer(PolicyInformation.class, new PolicyInformationSerializer());
		simpleModule.addDeserializer(PolicyInformation.class, new PolicyInformationDeserializer());
		simpleModule.addSerializer(ReactReview.class, new ReactReviewSerializer());
		simpleModule.addDeserializer(ReactReview.class, new ReactReviewDeserializer());
		simpleModule.addSerializer(ReactReviewTokenIssuerPublicIdentity.class,
				new ReactReviewTokenIssuerPublicIdentitySerializer());
		simpleModule.addDeserializer(ReactReviewTokenIssuerPublicIdentity.class,
				new ReactReviewTokenIssuerPublicIdentityDeserializer());
		simpleModule.addSerializer(ReactRepresentableReview.class, new ReactRepresentableReviewSerializer());
		simpleModule.addDeserializer(ReactRepresentableReview.class, new ReactRepresentableReviewDeserializer());
		simpleModule.addSerializer(ReactNonInteractivePolicyProof.class,
				new ReactNonInteractivePolicyProofSerializer());
		return simpleModule;
	}
}
