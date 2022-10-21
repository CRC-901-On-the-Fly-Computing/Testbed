package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "template" })
public class Template extends AbstractService implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("template")
    private Template_ template;
    private final static long serialVersionUID = -2832251846167619100L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("template")
    public Template_ getTemplate() {
        return this.template;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("template")
    public void setTemplate(final Template_ template) {
        this.template = template;
    }

    public Template withTemplate(final Template_ template) {
        this.template = template;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.template).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Template) == false) {
            return false;
        }
        final Template rhs = ((Template) other);
        return new EqualsBuilder().append(this.template, rhs.template).isEquals();
    }

}
