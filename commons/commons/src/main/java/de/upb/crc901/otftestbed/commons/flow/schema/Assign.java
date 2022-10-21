package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "assign" })
public class Assign extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assign")
    private Assign_ assign;
    private final static long serialVersionUID = 3242013884658322033L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assign")
    public Assign_ getAssign() {
        return this.assign;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assign")
    public void setAssign(final Assign_ assign) {
        this.assign = assign;
    }

    public Assign withAssign(final Assign_ assign) {
        this.assign = assign;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.assign).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Assign) == false) {
            return false;
        }
        final Assign rhs = ((Assign) other);
        return new EqualsBuilder().append(this.assign, rhs.assign).isEquals();
    }

}
