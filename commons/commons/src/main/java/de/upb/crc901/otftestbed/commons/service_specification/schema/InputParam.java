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
@JsonPropertyOrder({ "name", "dataType" })
public class InputParam implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
	@ApiModelProperty(position =0, example="f", required = true)
    @JsonProperty("name")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
	@ApiModelProperty(position =1, example="http://crc901.upb.de/MLTerms#Attribute_Vector", required = true)
    @JsonProperty("dataType")
    private String dataType;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5954027295651518942L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dataType")
    public String getDataType() {
        return this.dataType;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dataType")
    public void setDataType(final String dataType) {
        this.dataType = dataType;
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
        return new HashCodeBuilder().append(this.name).append(this.dataType).append(this.additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof InputParam) == false) {
            return false;
        }
        final InputParam rhs = ((InputParam) other);
        return new EqualsBuilder().append(this.name, rhs.name).append(this.dataType, rhs.dataType)
                .append(this.additionalProperties, rhs.additionalProperties).isEquals();
    }

}