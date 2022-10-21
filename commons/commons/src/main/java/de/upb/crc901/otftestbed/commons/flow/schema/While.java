package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "while" })
public class While extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("while")
    private While_ _while;
    private final static long serialVersionUID = -4507987496493999347L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("while")
    public While_ getWhile() {
        return this._while;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("while")
    public void setWhile(final While_ _while) {
        this._while = _while;
    }

    public While withWhile(final While_ _while) {
        this._while = _while;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this._while).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof While) == false) {
            return false;
        }
        final While rhs = ((While) other);
        return new EqualsBuilder().append(this._while, rhs._while).isEquals();
    }

}
