package de.upb.crc901.otftestbed.commons.requester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "serviceCompositionId", "serviceId" })
public class SimpleComposition {

	@JsonProperty("serviceCompositionId")
	private String serviceCompositionId;
	@JsonProperty("serviceId")
	private List<String> serviceId = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("serviceCompositionId")
	public String getServiceCompositionId() {
		return serviceCompositionId;
	}

	@JsonProperty("serviceCompositionId")
	public void setServiceCompositionId(String serviceCompositionId) {
		this.serviceCompositionId = serviceCompositionId;
	}

	@JsonProperty("serviceId")
	public List<String> getServiceId() {
		return serviceId;
	}

	@JsonProperty("serviceId")
	public void setServiceId(List<String> serviceId) {
		this.serviceId = serviceId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalProperties == null) ? 0 : additionalProperties.hashCode());
		result = prime * result + ((serviceCompositionId == null) ? 0 : serviceCompositionId.hashCode());
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleComposition other = (SimpleComposition) obj;
		if (additionalProperties == null) {
			if (other.additionalProperties != null)
				return false;
		} else if (!additionalProperties.equals(other.additionalProperties))
			return false;
		if (serviceCompositionId == null) {
			if (other.serviceCompositionId != null)
				return false;
		} else if (!serviceCompositionId.equals(other.serviceCompositionId))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleComposition [serviceCompositionId=" + serviceCompositionId + ", serviceId=" + serviceId
				+ ", additionalProperties=" + additionalProperties + "]";
	}

}