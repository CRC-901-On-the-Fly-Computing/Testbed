package de.upb.crc901.otftestbed.commons.service_specification.schema;

import java.io.Serializable;
import java.util.HashMap;
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
@JsonPropertyOrder({ "price" })
public class NonFunctionalProperties implements Serializable {
	@ApiModelProperty(position =1, example="80", required = false)
    @JsonProperty("price")
    private String price;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 9156901124337246747L;

    @JsonProperty("price")
    public String getPrice() {
        return this.price;
    }

    @JsonProperty("price")
    public void setPrice(final String price) {
        this.price = price;
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
        return new HashCodeBuilder().append(this.price).append(this.additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NonFunctionalProperties) == false) {
            return false;
        }
        final NonFunctionalProperties rhs = ((NonFunctionalProperties) other);
        return new EqualsBuilder().append(this.price, rhs.price)
                .append(this.additionalProperties, rhs.additionalProperties).isEquals();
    }

}