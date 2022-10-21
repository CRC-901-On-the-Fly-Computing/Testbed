package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "datatype" })
public class Lh extends JsonControlElement implements Serializable {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("datatype")
    private AbstractType datatype;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1584397574723437568L;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(final String name) {
        this.name = name;
    }

    public Lh withName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("datatype")
    public AbstractType getDatatype() {
        return this.datatype;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("datatype")
    public void setDatatype(final AbstractType datatype) {
        this.datatype = datatype;
    }

    public Lh withDatatype(final AbstractType datatype) {
        this.datatype = datatype;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }

    public Lh withAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.name).append(this.datatype).append(this.additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Lh) == false) {
            return false;
        }
        final Lh rhs = ((Lh) other);
        return new EqualsBuilder().append(this.name, rhs.name).append(this.datatype, rhs.datatype)
                .append(this.additionalProperties, rhs.additionalProperties).isEquals();
    }

}
