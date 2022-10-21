package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "inputConcreteVar", "inputAbstractVar" })
public class InputMapping extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputConcreteVar")
    private String inputConcreteVar;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputAbstractVar")
    private String inputAbstractVar;
    private final static long serialVersionUID = -2481342086013767713L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputConcreteVar")
    public String getInputConcreteVar() {
        return this.inputConcreteVar;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputConcreteVar")
    public void setInputConcreteVar(final String inputConcreteVar) {
        this.inputConcreteVar = inputConcreteVar;
    }

    public InputMapping withInputConcreteVar(final String inputConcreteVar) {
        this.inputConcreteVar = inputConcreteVar;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputAbstractVar")
    public String getInputAbstractVar() {
        return this.inputAbstractVar;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputAbstractVar")
    public void setInputAbstractVar(final String inputAbstractVar) {
        this.inputAbstractVar = inputAbstractVar;
    }

    public InputMapping withInputAbstractVar(final String inputAbstractVar) {
        this.inputAbstractVar = inputAbstractVar;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.inputConcreteVar).append(this.inputAbstractVar).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof InputMapping) == false) {
            return false;
        }
        final InputMapping rhs = ((InputMapping) other);
        return new EqualsBuilder().append(this.inputConcreteVar, rhs.inputConcreteVar)
                .append(this.inputAbstractVar, rhs.inputAbstractVar).isEquals();
    }

}
