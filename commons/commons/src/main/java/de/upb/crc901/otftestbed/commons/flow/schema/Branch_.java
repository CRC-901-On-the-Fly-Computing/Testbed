package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "condition", "if", "else" })
public class Branch_ extends JsonControlElement implements Serializable {

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
    @JsonProperty("if")
    private List<FlowElement> _if;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("else")
    private List<FlowElement> _else;
    private final static long serialVersionUID = -2021371306900691094L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    public Branch_ withId(final Integer id) {
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

    public Branch_ withCondition(final String condition) {
        this.condition = condition;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("if")
    public List<FlowElement> getIf() {
        return this._if;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("if")
    public void setIf(final List<FlowElement> _if) {
        this._if = _if;
    }

    public Branch_ withIf(final List<FlowElement> _if) {
        this._if = _if;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("else")
    public List<FlowElement> getElse() {
        return this._else;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("else")
    public void setElse(final List<FlowElement> _else) {
        this._else = _else;
    }

    public Branch_ withElse(final List<FlowElement> _else) {
        this._else = _else;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.condition).append(this._if).append(this._else)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Branch_) == false) {
            return false;
        }
        final Branch_ rhs = ((Branch_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.condition, rhs.condition)
                .append(this._if, rhs._if).append(this._else, rhs._else).isEquals();
    }

}
