package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "foreach" })
public class Foreach extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("foreach")
    private Foreach_ foreach;
    private final static long serialVersionUID = 6689403055730157922L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("foreach")
    public Foreach_ getForeach() {
        return this.foreach;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("foreach")
    public void setForeach(final Foreach_ foreach) {
        this.foreach = foreach;
    }

    public Foreach withForeach(final Foreach_ foreach) {
        this.foreach = foreach;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.foreach).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Foreach) == false) {
            return false;
        }
        final Foreach rhs = ((Foreach) other);
        return new EqualsBuilder().append(this.foreach, rhs.foreach).isEquals();
    }

}
