package de.upb.crc901.otftestbed.commons.service_specification.schema;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "operationName", "flowLink", "inputParams", "outputParams", "precondition", "effect",
		"nonFunctionalProperties" })
public class Operation implements Serializable {

	/**
	 * 
	 * (Required)
	 * 
	 */
	@ApiModelProperty(position = 0, required = true, example = "SVMClassifier")
	@JsonProperty("operationName")
	private String operationName;
	@ApiModelProperty(position=1, example="http://crc901.upb.de/SVMClassifier#Flow", required = false)
	@JsonProperty("flowLink")
	private String flowLink;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@ApiModelProperty(position = 2, required = true)
	@JsonProperty("inputParams")
	private List<InputParam> inputParams = null;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@ApiModelProperty(position = 3, required = true)
	@JsonProperty("outputParams")
	private List<OutputParam> outputParams = null;
	@ApiModelProperty(position =4, example="http://crc901.upb.de/SVMClassifier#Precondition", required = false)
	@JsonProperty("precondition")
	private String precondition;
	@ApiModelProperty(position =5, example="classifiedBy(f,b)", required = false)
	@JsonProperty("effect")
	private String effect;
	@ApiModelProperty(position =6, required = false)
	@JsonProperty("nonFunctionalProperties")
	private NonFunctionalProperties nonFunctionalProperties;
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 6685633591494749792L;

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("operationName")
	public String getOperationName() {
		return this.operationName;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("operationName")
	public void setOperationName(final String operationName) {
		this.operationName = operationName;
	}

	@JsonProperty("flowLink")
	public String getFlowLink() {
		return this.flowLink;
	}

	@JsonProperty("flowLink")
	public void setFlowLink(final String flowLink) {
		this.flowLink = flowLink;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("inputParams")
	public List<InputParam> getInputParams() {
		return this.inputParams;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("inputParams")
	public void setInputParams(final List<InputParam> inputParams) {
		this.inputParams = inputParams;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("outputParams")
	public List<OutputParam> getOutputParams() {
		return this.outputParams;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("outputParams")
	public void setOutputParams(final List<OutputParam> outputParams) {
		this.outputParams = outputParams;
	}

	@JsonProperty("precondition")
	public String getPrecondition() {
		return this.precondition;
	}

	@JsonProperty("precondition")
	public void setPrecondition(final String precondition) {
		this.precondition = precondition;
	}

	@JsonProperty("effect")
	public String getEffect() {
		return this.effect;
	}

	@JsonProperty("effect")
	public void setEffect(final String effect) {
		this.effect = effect;
	}

	@JsonProperty("nonFunctionalProperties")
	public NonFunctionalProperties getNonFunctionalProperties() {
		return this.nonFunctionalProperties;
	}

	@JsonProperty("nonFunctionalProperties")
	public void setNonFunctionalProperties(final NonFunctionalProperties nonFunctionalProperties) {
		this.nonFunctionalProperties = nonFunctionalProperties;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.operationName).append(this.flowLink).append(this.inputParams)
				.append(this.outputParams).append(this.precondition).append(this.effect)
				.append(this.nonFunctionalProperties).append(this.additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Operation) == false) {
			return false;
		}
		final Operation rhs = ((Operation) other);
		return new EqualsBuilder().append(this.operationName, rhs.operationName).append(this.flowLink, rhs.flowLink)
				.append(this.inputParams, rhs.inputParams).append(this.outputParams, rhs.outputParams)
				.append(this.precondition, rhs.precondition).append(this.effect, rhs.effect)
				.append(this.nonFunctionalProperties, rhs.nonFunctionalProperties)
				.append(this.additionalProperties, rhs.additionalProperties).isEquals();
	}

}