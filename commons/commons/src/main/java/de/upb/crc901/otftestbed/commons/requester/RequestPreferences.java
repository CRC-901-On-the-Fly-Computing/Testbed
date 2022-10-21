package de.upb.crc901.otftestbed.commons.requester;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "performance", "security", "usability" })
public class RequestPreferences {

	@JsonProperty("performance")
	private Integer performance;
	@JsonProperty("security")
	private Integer security;
	@JsonProperty("usability")
	private Integer usability;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("performance")
	public Integer getPerformance() {
		return performance;
	}

	@JsonProperty("performance")
	public void setPerformance(Integer performance) {
		this.performance = performance;
	}

	@JsonProperty("security")
	public Integer getSecurity() {
		return security;
	}

	@JsonProperty("security")
	public void setSecurity(Integer security) {
		this.security = security;
	}

	@JsonProperty("usability")
	public Integer getUsability() {
		return usability;
	}

	@JsonProperty("usability")
	public void setUsability(Integer usability) {
		this.usability = usability;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}