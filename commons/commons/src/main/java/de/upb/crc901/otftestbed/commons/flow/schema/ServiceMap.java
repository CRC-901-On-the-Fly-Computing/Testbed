package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "concreteServicecall", "abstractServicecall", "inputMapping", "outputMapping" })
public class ServiceMap extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteServicecall")
    private String concreteServicecall;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractServicecall")
    private String abstractServicecall;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputMapping")
    private List<InputMapping> inputMapping = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputMapping")
    private List<OutputMapping> outputMapping = null;
    private final static long serialVersionUID = -6343362460872090993L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteServicecall")
    public String getConcreteServicecall() {
        return this.concreteServicecall;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteServicecall")
    public void setConcreteServicecall(final String concreteServicecall) {
        this.concreteServicecall = concreteServicecall;
    }

    public ServiceMap withConcreteServicecall(final String concreteServicecall) {
        this.concreteServicecall = concreteServicecall;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractServicecall")
    public String getAbstractServicecall() {
        return this.abstractServicecall;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractServicecall")
    public void setAbstractServicecall(final String abstractServicecall) {
        this.abstractServicecall = abstractServicecall;
    }

    public ServiceMap withAbstractServicecall(final String abstractServicecall) {
        this.abstractServicecall = abstractServicecall;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputMapping")
    public List<InputMapping> getInputMapping() {
        return this.inputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputMapping")
    public void setInputMapping(final List<InputMapping> inputMapping) {
        this.inputMapping = inputMapping;
    }

    public ServiceMap withInputMapping(final List<InputMapping> inputMapping) {
        this.inputMapping = inputMapping;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputMapping")
    public List<OutputMapping> getOutputMapping() {
        return this.outputMapping;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputMapping")
    public void setOutputMapping(final List<OutputMapping> outputMapping) {
        this.outputMapping = outputMapping;
    }

    public ServiceMap withOutputMapping(final List<OutputMapping> outputMapping) {
        this.outputMapping = outputMapping;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.concreteServicecall).append(this.abstractServicecall)
                .append(this.inputMapping).append(this.outputMapping).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ServiceMap) == false) {
            return false;
        }
        final ServiceMap rhs = ((ServiceMap) other);
        return new EqualsBuilder().append(this.concreteServicecall, rhs.concreteServicecall)
                .append(this.abstractServicecall, rhs.abstractServicecall).append(this.inputMapping, rhs.inputMapping)
                .append(this.outputMapping, rhs.outputMapping).isEquals();
    }

}
