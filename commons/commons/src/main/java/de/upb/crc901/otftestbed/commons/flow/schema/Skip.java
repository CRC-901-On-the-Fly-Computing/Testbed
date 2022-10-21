package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "skip" })
public class Skip extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("skip")
    private Skip_ skip;
    private final static long serialVersionUID = 85420804687677464L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("skip")
    public Skip_ getSkip() {
        return this.skip;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("skip")
    public void setSkip(final Skip_ skip) {
        this.skip = skip;
    }

    public Skip withSkip(final Skip_ skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.skip).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Skip) == false) {
            return false;
        }
        final Skip rhs = ((Skip) other);
        return new EqualsBuilder().append(this.skip, rhs.skip).isEquals();
    }

}
