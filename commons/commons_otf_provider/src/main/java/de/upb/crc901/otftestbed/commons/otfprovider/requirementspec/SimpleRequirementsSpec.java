package de.upb.crc901.otftestbed.commons.otfprovider.requirementspec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation.OTFReputationRequest;
import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements.RequirementsSpecLiteral;
import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements.RequirementsSpecParameter;
import io.swagger.annotations.ApiModelProperty;

/**
 * A RequirementsSpec for the "non-expert user". MatchingSpecification,
 * ExplorationStrategy, and timeout are not part of this one and will be
 * specified by the RequestProcessor itself.
 *
 * @author mcp
 *
 */
public class SimpleRequirementsSpec {
	// TODO actually, this should be the "true" RequirementsSpec and the current
	// RequirementsSpec should be "ExtendedRequirementsSpec" or something
	// similar

	/**
	 * Name of the operation that should be created.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "q1")
	protected String operationName = "";

	/**
	 * Input parameters needed for the service.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "[{\"name\":\"i\",\"datatype\":\"http://crc901.upb.de/MLTerms#Image\"}]")
	protected List<RequirementsSpecParameter> inputParams = null;

	/**
	 * Output parameters needed for the service.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "[{\"name\":\"g\",\"datatype\":\"http://crc901.upb.de/MLTerms#Gender\"}]")
	protected List<RequirementsSpecParameter> outputParams = null;

	/**
	 * Precondition for the service.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "[]")
	protected List<RequirementsSpecLiteral> preCondition = null;

	/**
	 * Effect of the service.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "[{\"name\":\"faceRec\",\"params\":[\"i\",\"g\"]}]")
	protected List<RequirementsSpecLiteral> effect = null;

	/**
	 * Non functional requirements the service must met.
	 */
	@JsonProperty(required = true)
	@ApiModelProperty(example = "[60]")
	protected Number[] nonFunctionalRequirements = null;

	/**
	 * Creator of the requirements specification.
	 */
	@ApiModelProperty(example = "http://sfb-k8master-1.cs.uni-paderborn.de:30090/api/requester")
	protected String requester = null;

	/**
	 * Reputation requests. Those requests contain informations about the services
	 * that should be used to build this service.
	 */
	@JsonProperty(required = false)
	// may be null
	@ApiModelProperty(example = "[{\"reputationConditions\":[{\"context\":\"OTHER\",\"requestedFiveStarRange\":\"FOUR\",\"operator\":\"APPROX_GREATER_OR_EQUAL\"}],\"matchingResultReq\":{\"matchingType\":\"OTFFuzzyMatchingResultRequirement\",\"minLowerBound\":0.5,\"minUpperBound\":0.5}}]")
	protected List<OTFReputationRequest> reputationRequest = null;

	/**
	 * Empty C'tor for de-/serialization
	 */
	public SimpleRequirementsSpec() {
		// intentionally left empty
	}

	@Override
	public String toString() {
		return "SimpleRequirementsSpec [operationName=" + operationName + ", inputParams=" + inputParams
				+ ", outputParams=" + outputParams + ", preCondition=" + preCondition + ", effect=" + effect
				+ ", nonFunctionalRequirements=" + Arrays.toString(nonFunctionalRequirements) + ", requester="
				+ requester + ", reputationRequest=" + reputationRequest + "]";
	}

	// TODO: JavaDoc, method checks if no field is null
	public boolean allFieldsSet() {
		if (operationName == null) {
			return false;
		}
		if (nonFunctionalRequirements == null) {
			return false;
		}
		if (requester == null) {
			return false;
		}

		return true;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public List<RequirementsSpecParameter> getInputParams() {
		if (inputParams == null) {
			inputParams = new ArrayList<>();
		}
		return inputParams;
	}

	public List<RequirementsSpecParameter> getOutputParams() {
		if (outputParams == null) {
			outputParams = new ArrayList<>();
		}
		return outputParams;
	}

	public List<RequirementsSpecLiteral> getPreCondition() {
		if (preCondition == null) {
			preCondition = new ArrayList<>();
		}
		return preCondition;
	}

	public List<RequirementsSpecLiteral> getEffect() {
		if (effect == null) {
			effect = new ArrayList<>();
		}
		return effect;
	}

	public Number[] getNonFunctionalRequirements() {
		return nonFunctionalRequirements;
	}

	public void setNonFunctionalRequirements(Number[] nonFunctionalRequirements) {
		this.nonFunctionalRequirements = nonFunctionalRequirements;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public List<OTFReputationRequest> getReputationRequest() {
		if (reputationRequest == null) {
			reputationRequest = new ArrayList<>();
		}
		return reputationRequest;
	}
}
