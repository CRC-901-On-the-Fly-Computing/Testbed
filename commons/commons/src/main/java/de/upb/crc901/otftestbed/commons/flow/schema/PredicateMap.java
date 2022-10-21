package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "abstractPredicate", "abstractInputParameter", "concretePredicate" })
public class PredicateMap extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractPredicate")
    private String abstractPredicate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractInputParameter")
    private List<String> abstractInputParameter = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concretePredicate")
    private String concretePredicate;
    private final static long serialVersionUID = 4318609470524660385L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractPredicate")
    public String getAbstractPredicate() {
        return this.abstractPredicate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractPredicate")
    public void setAbstractPredicate(final String abstractPredicate) {
        this.abstractPredicate = abstractPredicate;
    }

    public PredicateMap withAbstractPredicate(final String abstractPredicate) {
        this.abstractPredicate = abstractPredicate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractInputParameter")
    public List<String> getAbstractInputParameter() {
        return this.abstractInputParameter;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("abstractInputParameter")
    public void setAbstractInputParameter(final List<String> abstractInputParameter) {
        this.abstractInputParameter = abstractInputParameter;
    }

    public PredicateMap withAbstractInputParameter(final List<String> abstractInputParameter) {
        this.abstractInputParameter = abstractInputParameter;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concretePredicate")
    public String getConcretePredicate() {
        return this.concretePredicate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("concretePredicate")
    public void setConcretePredicate(final String concretePredicate) {
        this.concretePredicate = concretePredicate;
    }

    public PredicateMap withConcretePredicate(final String concretePredicate) {
        this.concretePredicate = concretePredicate;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.abstractPredicate).append(this.abstractInputParameter)
                .append(this.concretePredicate).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PredicateMap) == false) {
            return false;
        }
        final PredicateMap rhs = ((PredicateMap) other);
        return new EqualsBuilder().append(this.abstractPredicate, rhs.abstractPredicate)
                .append(this.abstractInputParameter, rhs.abstractInputParameter)
                .append(this.concretePredicate, rhs.concretePredicate).isEquals();
    }

}
