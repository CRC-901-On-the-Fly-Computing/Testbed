package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "branch" })
public class Branch extends FlowElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("branch")
    private Branch_ branch;
    private final static long serialVersionUID = 2982493758673748174L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("branch")
    public Branch_ getBranch() {
        return this.branch;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("branch")
    public void setBranch(final Branch_ branch) {
        this.branch = branch;
    }

    public Branch withBranch(final Branch_ branch) {
        this.branch = branch;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.branch).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Branch) == false) {
            return false;
        }
        final Branch rhs = ((Branch) other);
        return new EqualsBuilder().append(this.branch, rhs.branch).isEquals();
    }

}
