package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "var", "set", "invariant", "body" })
public class Foreach_ extends JsonControlElement implements Serializable {

    @JsonProperty("id")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("var")
    private String var;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("set")
    private String set;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invariant")
    private String invariant;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("body")
    private List<FlowElement> body;
    private final static long serialVersionUID = 8879161583849842296L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    public Foreach_ withId(final Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("var")
    public String getVar() {
        return this.var;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("var")
    public void setVar(final String var) {
        this.var = var;
    }

    public Foreach_ withVar(final String var) {
        this.var = var;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("set")
    public String getSet() {
        return this.set;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("set")
    public void setSet(final String set) {
        this.set = set;
    }

    public Foreach_ withSet(final String set) {
        this.set = set;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invariant")
    public String getInvariant() {
        return this.invariant;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invariant")
    public void setInvariant(final String invariant) {
        this.invariant = invariant;
    }

    public Foreach_ withInvariant(final String invariant) {
        this.invariant = invariant;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("body")
    public List<FlowElement> getBody() {
        return this.body;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("body")
    public void setBody(final List<FlowElement> body) {
        this.body = body;
    }

    public Foreach_ withBody(final List<FlowElement> body) {
        this.body = body;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.var).append(this.set).append(this.invariant)
                .append(this.body).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Foreach_) == false) {
            return false;
        }
        final Foreach_ rhs = ((Foreach_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.var, rhs.var).append(this.set, rhs.set)
                .append(this.invariant, rhs.invariant).append(this.body, rhs.body).isEquals();
    }

}
