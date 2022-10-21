package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "serviceID", "operationName", "input", "output" })
public class Service_ extends JsonControlElement implements Serializable {

    @JsonProperty("id")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceID")
    private String serviceID;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("operationName")
    private String operationName;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    private List<String> input = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    private List<String> output = null;
    private final static long serialVersionUID = -180727784874112084L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceID")
    public String getServiceID() {
        return this.serviceID;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceID")
    public void setServiceID(final String serviceID) {
        this.serviceID = serviceID;
    }

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

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public List<String> getInput() {
        return this.input;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public void setInput(final List<String> input) {
        this.input = input;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    public List<String> getOutput() {
        return this.output;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    public void setOutput(final List<String> output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.serviceID).append(this.operationName)
                .append(this.input).append(this.output).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Service_) == false) {
            return false;
        }
        final Service_ rhs = ((Service_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.serviceID, rhs.serviceID)
                .append(this.operationName, rhs.operationName).append(this.input, rhs.input)
                .append(this.output, rhs.output).isEquals();
    }

}
