package de.upb.crc901.otftestbed.commons.requester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crypto.interfaces.policy.Policy;
import de.upb.crypto.interfaces.policy.ThresholdPolicy;
import de.upb.crypto.math.interfaces.structures.Element;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.buildingblocks.attributes.AttributeDefinition;
import de.upb.crypto.react.buildingblocks.attributes.AttributeNameValuePair;
import de.upb.crypto.react.buildingblocks.attributes.AttributeSpace;
import de.upb.crypto.react.buildingblocks.attributes.StringAttributeDefinition;
import de.upb.crypto.react.predicategeneration.fixedprotocols.PredicateTypePrimitive;
import de.upb.crypto.react.predicategeneration.policies.PredicatePolicyFact;
import de.upb.crypto.react.predicategeneration.policies.SubPolicyPolicyFact;
import de.upb.crypto.react.predicategeneration.rangeproofs.ArbitraryRangeProofPublicParameters;
import de.upb.crypto.react.predicategeneration.setmembershipproofs.SetMembershipPublicParameters;
import de.upb.crypto.react.protocols.accumulators.nguyen.NguyenAccumulatorIdentity;

public class SimplePolicy {

	@JsonProperty("allCountries")
	List<String> allCountries;

	@JsonProperty("allowedCountries")
	List<String> allowedCountries;

	@JsonProperty("minAge")
	int minAge;

	@JsonProperty("maxAge")
	int maxAge;

	@JsonProperty("allLicences")
	List<String> allLicences;

	@JsonProperty("allowedLicenses")
	List<String> allowedLicences;

	@JsonProperty("allSubsciptionLevels")
	List<String> allSubscriptionLevels;

	@JsonProperty("allowedSubscriptionLevels")
	List<String> allowedSubscriptionLevels;

	public SimplePolicy() {
	}

	@JsonIgnore
	public static SimplePolicy fromPolicyInformation(PolicyInformation pI, ReactPublicParameters pp) {
		SimplePolicy simplePolicy = new SimplePolicy();

		AttributeSpace attributes = pI.getUsedAttributeSpaces().get(0);
		ThresholdPolicy policy = (ThresholdPolicy) pI.getPolicy();
		for (Policy fact : policy.getChildren()) {
			SubPolicyPolicyFact subFact = (SubPolicyPolicyFact) fact;
			for (Policy subsubPolicy : subFact.getSubPolicy().getChildren()) {
				PredicatePolicyFact ppFact = (PredicatePolicyFact) subsubPolicy;
				if (ppFact.getProofType() == PredicateTypePrimitive.ATTRIBUTE_IN_RANGE) {
					ArbitraryRangeProofPublicParameters predicatePP = (ArbitraryRangeProofPublicParameters) ppFact
							.getPublicParameters();
					/* To resolve the name */
					int indexInAttributeSpace = predicatePP.getPositionOfCommitment();
					AttributeDefinition space = attributes.getDefinitions().get(indexInAttributeSpace);
					if ("age".equals(space.getAttributeName())) {
						simplePolicy.minAge = predicatePP.getLowerBound().intValue();
						simplePolicy.maxAge = predicatePP.getUpperBound().intValue();
					}

				} else if (ppFact.getProofType() == PredicateTypePrimitive.SET_MEMBERSHIP_ATTRIBUTE) {
					SetMembershipPublicParameters setMembershipPP = (SetMembershipPublicParameters) ppFact
							.getPublicParameters();
					int indexInAttributeSpace = setMembershipPP.getPositionOfCommitment();
					StringAttributeDefinition space = (StringAttributeDefinition) attributes.getDefinitions()
							.get(indexInAttributeSpace);
					String regEx = space.getRegularExpressionPattern().pattern();
					String[] attributeValues = regEx.substring(1, regEx.length() - 1).split("\\|");

					List<String> allValues = new ArrayList<>();

					Map<Element, String> hashToName = new HashMap<>();
					for (String attributeValue : attributeValues) {
						AttributeNameValuePair anvp = space.createAttribute(attributeValue);
						hashToName.put(pp.getHashIntoZp().hashIntoStructure(anvp), attributeValue);
						allValues.add(attributeValue);
					}

					List<String> allowedValues = new ArrayList<>();

					for (NguyenAccumulatorIdentity attributeInPolicy : setMembershipPP.getSetMembers()) {
						String name = hashToName.get(attributeInPolicy.getIdentity());
						allowedValues.add(name);
					}

					if ("userLicense".equals(space.getAttributeName())) {
						simplePolicy.allLicences = allValues;
						simplePolicy.allowedLicences = allowedValues;
					} else if ("country".equals(space.getAttributeName())) {
						simplePolicy.allCountries = allValues;
						simplePolicy.allowedCountries = allowedValues;
					} else if ("subscriptionLevel".equals(space.getAttributeName())) {
						simplePolicy.allSubscriptionLevels = allValues;
						simplePolicy.allowedSubscriptionLevels = allowedValues;
					}
				}
			}
		}
		return simplePolicy;

	}

	public List<String> getAllCountries() {
		return allCountries;
	}

	public void setAllCountries(List<String> allCountries) {
		this.allCountries = allCountries;
	}

	public List<String> getAllowedCountries() {
		return allowedCountries;
	}

	public void setAllowedCountries(List<String> allowedCountries) {
		this.allowedCountries = allowedCountries;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public List<String> getAllLicences() {
		return allLicences;
	}

	public void setAllLicences(List<String> allLicences) {
		this.allLicences = allLicences;
	}

	public List<String> getAllowedLicences() {
		return allowedLicences;
	}

	public void setAllowedLicences(List<String> allowedLicences) {
		this.allowedLicences = allowedLicences;
	}

	public List<String> getAllSubscriptionLevels() {
		return allSubscriptionLevels;
	}

	public void setAllSubscriptionLevels(List<String> allSubscriptionLevels) {
		this.allSubscriptionLevels = allSubscriptionLevels;
	}

	public List<String> getAllowedSubscriptionLevels() {
		return allowedSubscriptionLevels;
	}

	public void setAllowedSubscriptionLevels(List<String> allowedSubscriptionLevels) {
		this.allowedSubscriptionLevels = allowedSubscriptionLevels;
	}
}
