package de.upb.crc901.otftestbed.commons.service_specification.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "service_specifications")
@JsonPropertyOrder({ "serviceName", "realizationLink", "operations" })
public class ServiceSpecification implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
	@ApiModelProperty(position= 0, example="SVMClassifier", required = true)
    @JsonProperty("serviceName")
    private String serviceName;
	@ApiModelProperty(position = 1, example="http://crc901.upb.de/SVMClassifier", required = false)
    @JsonProperty("realizationLink")
    private String realizationLink;
    /**
     * 
     * (Required)
     * 
     */
	@ApiModelProperty(position=2, required = true)
    @JsonProperty("operations")
    private List<Operation> operations = null;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2496904573293249630L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceName")
    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceName")
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    @JsonProperty("realizationLink")
    public String getRealizationLink() {
        return this.realizationLink;
    }

    @JsonProperty("realizationLink")
    public void setRealizationLink(final String realizationLink) {
        this.realizationLink = realizationLink;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("operations")
    public List<Operation> getOperations() {
        return this.operations;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("operations")
    public void setOperations(final List<Operation> operations) {
        this.operations = operations;
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
        return new HashCodeBuilder().append(this.serviceName).append(this.realizationLink).append(this.operations)
                .append(this.additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ServiceSpecification) == false) {
            return false;
        }
        final ServiceSpecification rhs = ((ServiceSpecification) other);
        return new EqualsBuilder().append(this.serviceName, rhs.serviceName)
                .append(this.realizationLink, rhs.realizationLink).append(this.operations, rhs.operations)
                .append(this.additionalProperties, rhs.additionalProperties).isEquals();
    }

}