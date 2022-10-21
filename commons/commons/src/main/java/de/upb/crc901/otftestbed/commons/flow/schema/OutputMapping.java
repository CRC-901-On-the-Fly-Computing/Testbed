package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "outputConcreteVar", "outputAbstractVar" })
public class OutputMapping extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputConcreteVar")
    private String outputConcreteVar;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputAbstractVar")
    private String outputAbstractVar;
    private final static long serialVersionUID = 2447243963987063186L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputConcreteVar")
    public String getOutputConcreteVar() {
        return this.outputConcreteVar;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputConcreteVar")
    public void setOutputConcreteVar(final String outputConcreteVar) {
        this.outputConcreteVar = outputConcreteVar;
    }

    public OutputMapping withOutputConcreteVar(final String outputConcreteVar) {
        this.outputConcreteVar = outputConcreteVar;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputAbstractVar")
    public String getOutputAbstractVar() {
        return this.outputAbstractVar;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("outputAbstractVar")
    public void setOutputAbstractVar(final String outputAbstractVar) {
        this.outputAbstractVar = outputAbstractVar;
    }

    public OutputMapping withOutputAbstractVar(final String outputAbstractVar) {
        this.outputAbstractVar = outputAbstractVar;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.outputConcreteVar).append(this.outputAbstractVar).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutputMapping) == false) {
            return false;
        }
        final OutputMapping rhs = ((OutputMapping) other);
        return new EqualsBuilder().append(this.outputConcreteVar, rhs.outputConcreteVar)
                .append(this.outputAbstractVar, rhs.outputAbstractVar).isEquals();
    }

}
