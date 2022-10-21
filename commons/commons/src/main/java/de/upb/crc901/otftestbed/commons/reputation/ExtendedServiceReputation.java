package de.upb.crc901.otftestbed.commons.reputation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class ExtendedServiceReputation {

	@ApiModelProperty(example = "1")
	private double overall = 0;
	@ApiModelProperty(example = "2")
	private double usability = 0;
	@ApiModelProperty(example = "3")
	private double performance = 0;
	@ApiModelProperty(example = "4")
	private double security = 0;
	@ApiModelProperty(example = "5")
	private double other = 0;
	@ApiModelProperty(example = "Simply Amazing ;) ")
	private String reputationMessage = "";

	private Date date = null;

	private UUID providerUUID = null;

	private String serviceCompositionId = null;

	private List<String> services;

	@JsonCreator
	public ExtendedServiceReputation(@JsonProperty("overall") double overall,
			@JsonProperty("usability") double usability, @JsonProperty("performance") double performance,
			@JsonProperty("security") double security, @JsonProperty("other") double other,
			@JsonProperty("reputationMessage") String reputationMessage, @JsonProperty("date") Date date,
			@JsonProperty("providerUUID") UUID providerUUID,
			@JsonProperty("serviceCompositionId") String serviceCompositionId,
			@JsonProperty("services") List<String> services) {
		this.overall = overall;
		this.usability = usability;
		this.performance = performance;
		this.security = security;
		this.other = other;
		this.reputationMessage = reputationMessage;
		this.date = date;
		this.providerUUID = providerUUID;
		this.serviceCompositionId = serviceCompositionId;
		this.services = services;
	}

	@JsonIgnore
	public ExtendedServiceReputation(ServiceReputation reputation, UUID providerUUID, String serviceCompositionId,
			List<String> services) {
		this(reputation.getOverall(), reputation.getUsability(), reputation.getPerformance(), reputation.getSecurity(),
				reputation.getOther(), reputation.getReputationMessage(), reputation.getDate(), providerUUID,
				serviceCompositionId, services);
	}

	public ExtendedServiceReputation() {
		// needed for deserialization
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public double getUsability() {
		return usability;
	}

	public void setUsability(double usability) {
		this.usability = usability;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance(double performance) {
		this.performance = performance;
	}

	public double getSecurity() {
		return security;
	}

	public void setSecurity(double security) {
		this.security = security;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public String getReputationMessage() {
		return reputationMessage;
	}

	public void setReputationMessage(String reputationMessage) {
		this.reputationMessage = reputationMessage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UUID getProviderUUID() {
		return providerUUID;
	}

	public void setProviderUUID(UUID providerUUID) {
		this.providerUUID = providerUUID;
	}

	public String getServiceCompositionId() {
		return serviceCompositionId;
	}

	public void setServiceCompositionId(String serviceCompositionId) {
		this.serviceCompositionId = serviceCompositionId;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}
}
