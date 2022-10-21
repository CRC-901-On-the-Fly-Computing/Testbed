package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "gtype", "generic" })
public class GenericType extends AbstractType implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gtype")
    private String gtype;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("generic")
    private AbstractType generic;
    private final static long serialVersionUID = 5390868278045738934L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gtype")
    public String getGtype() {
        return this.gtype;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gtype")
    public void setGtype(final String gtype) {
        this.gtype = gtype;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("generic")
    public AbstractType getGeneric() {
        return this.generic;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("generic")
    public void setGeneric(final AbstractType generic) {
        this.generic = generic;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.gtype).append(this.generic).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GenericType) == false) {
            return false;
        }
        final GenericType rhs = ((GenericType) other);
        return new EqualsBuilder().append(this.gtype, rhs.gtype).append(this.generic, rhs.generic).isEquals();
    }

}
