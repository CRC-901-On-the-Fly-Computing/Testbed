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
@JsonPropertyOrder({ "serviceDescriptionLink", "serviceOperationName", "flowInputMapping", "flowOutputMapping", "flow" })
public class Flow extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceDescriptionLink")
    private String serviceDescriptionLink;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceOperationName")
    private String serviceOperationName;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowInputMapping")
    private List<String> flowInputMapping = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowOutputMapping")
    private List<String> flowOutputMapping = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flow")
    private List<FlowElement> flow = null;
    private final static long serialVersionUID = -5120069293908058257L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceDescriptionLink")
    public String getServiceDescriptionLink() {
        return this.serviceDescriptionLink;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceDescriptionLink")
    public void setServiceDescriptionLink(final String serviceDescriptionLink) {
        this.serviceDescriptionLink = serviceDescriptionLink;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceOperationName")
    public String getServiceOperationName() {
        return this.serviceOperationName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceOperationName")
    public void setServiceOperationName(final String serviceOperationName) {
        this.serviceOperationName = serviceOperationName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowInputMapping")
    public List<String> getFlowInputMapping() {
        return this.flowInputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowInputMapping")
    public void setFlowInputMapping(final List<String> flowInputMapping) {
        this.flowInputMapping = flowInputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowOutputMapping")
    public List<String> getFlowOutputMapping() {
        return this.flowOutputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flowOutputMapping")
    public void setFlowOutputMapping(final List<String> flowOutputMapping) {
        this.flowOutputMapping = flowOutputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flow")
    public List<FlowElement> getFlow() {
        return this.flow;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("flow")
    public void setFlow(final List<FlowElement> flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.serviceDescriptionLink).append(this.serviceOperationName)
                .append(this.flowInputMapping).append(this.flowOutputMapping).append(this.flow).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Flow) == false) {
            return false;
        }
        final Flow rhs = ((Flow) other);
        return new EqualsBuilder().append(this.serviceDescriptionLink, rhs.serviceDescriptionLink)
                .append(this.serviceOperationName, rhs.serviceOperationName)
                .append(this.flowInputMapping, rhs.flowInputMapping)
                .append(this.flowOutputMapping, rhs.flowOutputMapping).append(this.flow, rhs.flow).isEquals();
    }

}
