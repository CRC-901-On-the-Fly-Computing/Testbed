package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "simpleType" })
public class SimpleType extends AbstractType implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("simpleType")
    private String simpleType;
    private final static long serialVersionUID = -8206276356690962818L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("simpleType")
    public String getSimpleType() {
        return this.simpleType;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("simpleType")
    public void setSimpleType(final String simpleType) {
        this.simpleType = simpleType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.simpleType).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SimpleType) == false) {
            return false;
        }
        final SimpleType rhs = ((SimpleType) other);
        return new EqualsBuilder().append(this.simpleType, rhs.simpleType).isEquals();
    }

}