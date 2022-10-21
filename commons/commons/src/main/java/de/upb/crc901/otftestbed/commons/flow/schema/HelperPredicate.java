package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "concreteHelperPredicate", "abstractHelperPredicate" })
public class HelperPredicate extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteHelperPredicate")
    private String concreteHelperPredicate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractHelperPredicate")
    private String abstractHelperPredicate;
    private final static long serialVersionUID = 7849713288414274512L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteHelperPredicate")
    public String getConcreteHelperPredicate() {
        return this.concreteHelperPredicate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concreteHelperPredicate")
    public void setConcreteHelperPredicate(final String concreteHelperPredicate) {
        this.concreteHelperPredicate = concreteHelperPredicate;
    }

    public HelperPredicate withConcreteHelperPredicate(final String concreteHelperPredicate) {
        this.concreteHelperPredicate = concreteHelperPredicate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractHelperPredicate")
    public String getAbstractHelperPredicate() {
        return this.abstractHelperPredicate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractHelperPredicate")
    public void setAbstractHelperPredicate(final String abstractHelperPredicate) {
        this.abstractHelperPredicate = abstractHelperPredicate;
    }

    public HelperPredicate withAbstractHelperPredicate(final String abstractHelperPredicate) {
        this.abstractHelperPredicate = abstractHelperPredicate;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.concreteHelperPredicate).append(this.abstractHelperPredicate)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HelperPredicate) == false) {
            return false;
        }
        final HelperPredicate rhs = ((HelperPredicate) other);
        return new EqualsBuilder().append(this.concreteHelperPredicate, rhs.concreteHelperPredicate)
                .append(this.abstractHelperPredicate, rhs.abstractHelperPredicate).isEquals();
    }

}
