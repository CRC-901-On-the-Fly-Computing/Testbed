package de.upb.crc901.otftestbed.commons.flow.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "templateID", "input", "output", "serviceMap", "predicateMap", "helperPredicates" })
public class Template_ extends JsonControlElement implements Serializable {

    @JsonProperty("id")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("templateID")
    private String templateID;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    private List<String> input = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    private List<String> output = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceMap")
    private List<ServiceMap> serviceMap = null;
    @JsonProperty("predicateMap")
    private List<PredicateMap> predicateMap = null;
    @JsonProperty("helperPredicates")
    private List<HelperPredicate> helperPredicates = null;
    private final static long serialVersionUID = 4551833995462937252L;

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    public Template_ withId(final Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("templateID")
    public String getTemplateID() {
        return this.templateID;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("templateID")
    public void setTemplateID(final String templateID) {
        this.templateID = templateID;
    }

    public Template_ withTemplateID(final String templateID) {
        this.templateID = templateID;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public List<String> getInput() {
        return this.input;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("input")
    public void setInput(final List<String> input) {
        this.input = input;
    }

    public Template_ withInput(final List<String> input) {
        this.input = input;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    public List<String> getOutput() {
        return this.output;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("output")
    public void setOutput(final List<String> output) {
        this.output = output;
    }

    public Template_ withOutput(final List<String> output) {
        this.output = output;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceMap")
    public List<ServiceMap> getServiceMap() {
        return this.serviceMap;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("serviceMap")
    public void setServiceMap(final List<ServiceMap> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Template_ withServiceMap(final List<ServiceMap> serviceMap) {
        this.serviceMap = serviceMap;
        return this;
    }

    @JsonProperty("predicateMap")
    public List<PredicateMap> getPredicateMap() {
        return this.predicateMap;
    }

    @JsonProperty("predicateMap")
    public void setPredicateMap(final List<PredicateMap> predicateMap) {
        this.predicateMap = predicateMap;
    }

    public Template_ withPredicateMap(final List<PredicateMap> predicateMap) {
        this.predicateMap = predicateMap;
        return this;
    }

    @JsonProperty("helperPredicates")
    public List<HelperPredicate> getHelperPredicates() {
        return this.helperPredicates;
    }

    @JsonProperty("helperPredicates")
    public void setHelperPredicates(final List<HelperPredicate> helperPredicates) {
        this.helperPredicates = helperPredicates;
    }

    public Template_ withHelperPredicates(final List<HelperPredicate> helperPredicates) {
        this.helperPredicates = helperPredicates;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.templateID).append(this.input).append(this.output)
                .append(this.serviceMap).append(this.predicateMap).append(this.helperPredicates).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Template_) == false) {
            return false;
        }
        final Template_ rhs = ((Template_) other);
        return new EqualsBuilder().append(this.id, rhs.id).append(this.templateID, rhs.templateID)
                .append(this.input, rhs.input).append(this.output, rhs.output).append(this.serviceMap, rhs.serviceMap)
                .append(this.predicateMap, rhs.predicateMap).append(this.helperPredicates, rhs.helperPredicates)
                .isEquals();
    }

}
