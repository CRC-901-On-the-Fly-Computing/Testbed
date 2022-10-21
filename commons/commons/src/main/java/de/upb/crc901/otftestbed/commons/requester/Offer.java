package de.upb.crc901.otftestbed.commons.requester;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crypto.react.acs.policy.PolicyInformation;

@JsonPropertyOrder({ "offerUUID", "compositionID", "nonFunctionalProperties", "policyInformation",
		"compositionAndOTFProvider", "simplePolicy", "offerScore" })
public class Offer {

	@JsonProperty
	private Map<String, String> nonFunctionalProperties;

	@JsonProperty
	private UUID offerUUID;

	@JsonProperty
	private PolicyInformation policyInformation;

	@JsonProperty
	private CompositionAndOTFProvider compositionAndOTFProvider;

	@JsonProperty
	private String compositionID;

	@JsonProperty
	private SimplePolicy simplePolicy;

	@JsonProperty
	private double offerScore;

	public Offer() {
	}

	@JsonIgnore
	public PolicyInformation getPolicyInformation() {
		return policyInformation;
	}

	@JsonIgnore
	public CompositionAndOTFProvider getCompositionAndOTFProvider() {
		return compositionAndOTFProvider;
	}

	@JsonIgnore
	public String getCompositionID() {
		return compositionID;
	}

	@JsonIgnore
	public UUID getOfferUUID() {
		return offerUUID;
	}

	@JsonIgnore
	public Map<String, String> getNonFunctionalProperties() {
		return nonFunctionalProperties;
	}

	public void setOfferUUID(UUID offerUUID) {
		this.offerUUID = offerUUID;
	}

	public void setCompositionID(String compositionID) {
		this.compositionID = compositionID;
	}

	public void setNonFunctionalProperties(Map<String, String> nonFunctionalProperties) {
		this.nonFunctionalProperties = nonFunctionalProperties;
	}

	public void setPolicyInformation(PolicyInformation policyInformation) {
		this.policyInformation = policyInformation;
	}
	

	public void setCompositionAndOTFProvider(CompositionAndOTFProvider compositionAndOTFProvider) {
		this.compositionID = compositionAndOTFProvider.getSimpleComposition().getServiceCompositionId();
		this.compositionAndOTFProvider = compositionAndOTFProvider;
	}

	@Override
	public String toString() {
		return "Offer [nonFunctionalProperties=" + nonFunctionalProperties + ", offerUUID=" + offerUUID
				+ ", policyInformation=" + policyInformation + ", compositionAndOTFProvider="
				+ compositionAndOTFProvider + ", compositionID=" + compositionID + ", offerScore=" + offerScore + "]";
	}

	public SimplePolicy getSimplePolicy() {
		return simplePolicy;
	}

	public void setSimplePolicy(SimplePolicy simplePolicy) {
		this.simplePolicy = simplePolicy;
	}

	public double getOfferScore() {
		return offerScore;
	}

	public void setOfferScore(double offerScore) {
		this.offerScore = offerScore;
	}

}
