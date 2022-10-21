package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "condition", "invariant", "body" })
public class While_ extends JsonControlElement implements Serializable {

    @JsonProperty("id")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("condition")
    private String condition;
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
    private final static long serialVersionUID = 36671988252029026L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    public While_ withId(final Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("condition")
    public String getCondition() {
        return this.condition;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("condition")
    public void setCondition(final String condition) {
        this.condition = condition;
    }

    public While_ withCondition(final String condition) {
        this.condition = condition;
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

    public While_ withInvariant(final String invariant) {
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

    public While_ withBody(final List<FlowElement> body) {
        this.body = body;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.condition).append(this.invariant).append(this.body)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof While_) == false) {
            return false;
        }
        final While_ rhs = ((While_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.condition, rhs.condition)
                .append(this.invariant, rhs.invariant).append(this.body, rhs.body).isEquals();
    }

}
