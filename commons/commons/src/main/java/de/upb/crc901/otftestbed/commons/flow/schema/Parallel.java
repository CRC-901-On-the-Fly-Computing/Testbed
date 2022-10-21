package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "parallel" })
public class Parallel extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parallel")
    private List<AbstractService> parallel = null;
    private final static long serialVersionUID = 3251521291552472888L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parallel")
    public List<AbstractService> getParallel() {
        return this.parallel;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parallel")
    public void setParallel(final List<AbstractService> parallel) {
        this.parallel = parallel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.parallel).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Parallel) == false) {
            return false;
        }
        final Parallel rhs = ((Parallel) other);
        return new EqualsBuilder().append(this.parallel, rhs.parallel).isEquals();
    }

}
