package de.upb.crc901.otftestbed.commons.requester;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "build_version", "domain", "performance_metrics", "proba_autoML", "proba_gaming", "proba_other",
		"request_id" })
public class DomainPreferences {

	@JsonProperty("build_version")
	private String buildVersion;

	@JsonProperty("domain")
	private String domain;

	@JsonProperty("performance_metrics")
	private PerformanceMetrics performanceMetrics;

	@JsonProperty("proba_autoML")
	private Double probaAutoML;

	@JsonProperty("proba_gaming")
	private Double probaGaming;

	@JsonProperty("proba_other")
	private Double probaOther;

	@JsonProperty("request_id")
	private Integer requestId;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("build_version")
	public String getBuildVersion() {
		return buildVersion;
	}

	@JsonProperty("build_version")
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	@JsonProperty("domain")
	public String getDomain() {
		return domain;
	}

	@JsonProperty("domain")
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@JsonProperty("performance_metrics")
	public PerformanceMetrics getPerformanceMetrics() {
		return performanceMetrics;
	}

	@JsonProperty("performance_metrics")
	public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
		this.performanceMetrics = performanceMetrics;
	}

	@JsonProperty("proba_autoML")
	public Double getProbaAutoML() {
		return probaAutoML;
	}

	@JsonProperty("proba_autoML")
	public void setProbaAutoML(Double probaAutoML) {
		this.probaAutoML = probaAutoML;
	}

	@JsonProperty("proba_gaming")
	public Double getProbaGaming() {
		return probaGaming;
	}

	@JsonProperty("proba_gaming")
	public void setProbaGaming(Double probaGaming) {
		this.probaGaming = probaGaming;
	}

	@JsonProperty("proba_other")
	public Double getProbaOther() {
		return probaOther;
	}

	@JsonProperty("proba_other")
	public void setProbaOther(Double probaOther) {
		this.probaOther = probaOther;
	}

	@JsonProperty("request_id")
	public Integer getRequestId() {
		return requestId;
	}

	@JsonProperty("request_id")
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Map<String, String> getKnownFacts() {
		Map<String, String> knownFields = new HashMap<>();

		knownFields.put("domain", this.getDomain());
		knownFields.put("proba_autoML", this.probaAutoML+"");
		knownFields.put("proba_other", this.probaOther+"");
		knownFields.put("minLowerBound", "0");
		Map<?, ?> reputation = (Map<?, ?>) additionalProperties.get("reputation");
		knownFields.putAll(reputation.entrySet().stream()
				.collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue()))));

		return knownFields;
	}
}
