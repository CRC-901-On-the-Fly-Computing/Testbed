package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "service" })
public class Service extends AbstractService implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("service")
    private Service_ service;
    private final static long serialVersionUID = -8825671959018175985L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("service")
    public Service_ getService() {
        return this.service;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("service")
    public void setService(final Service_ service) {
        this.service = service;
    }

    public Service withService(final Service_ service) {
        this.service = service;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.service).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Service) == false) {
            return false;
        }
        final Service rhs = ((Service) other);
        return new EqualsBuilder().append(this.service, rhs.service).isEquals();
    }

}
