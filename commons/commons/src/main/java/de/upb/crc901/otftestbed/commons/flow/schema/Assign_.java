package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "lhs", "rhs" })
public class Assign_ extends JsonControlElement implements Serializable {

    @JsonProperty("id")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("lhs")
    private List<Lh> lhs = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rhs")
    private String rhs;
    private final static long serialVersionUID = -5732676641239103162L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    public Assign_ withId(final Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("lhs")
    public List<Lh> getLhs() {
        return this.lhs;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("lhs")
    public void setLhs(final List<Lh> lhs) {
        this.lhs = lhs;
    }

    public Assign_ withLhs(final List<Lh> lhs) {
        this.lhs = lhs;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rhs")
    public String getRhs() {
        return this.rhs;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rhs")
    public void setRhs(final String rhs) {
        this.rhs = rhs;
    }

    public Assign_ withRhs(final String rhs) {
        this.rhs = rhs;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.lhs).append(this.rhs).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Assign_) == false) {
            return false;
        }
        final Assign_ rhs = ((Assign_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.lhs, rhs.lhs).append(rhs, rhs.rhs).isEquals();
    }

}
