package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.annotations.ApiModel;

/**
 * Abstract result requirement class.
 *
 * @author jonsen94
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
    property = "matchingType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OTFSimpleMatchingResultRequirement.class,
        name = "OTFSimpleMatchingResultRequirement"),
    @JsonSubTypes.Type(value = OTFFuzzyMatchingResultRequirement.class,
        name = "OTFFuzzyMatchingResultRequirement")})
@ApiModel(value = "OTFMatchingResultRequirement",
    subTypes = {OTFSimpleMatchingResultRequirement.class, OTFFuzzyMatchingResultRequirement.class},
    discriminator = "matchingType")
public interface OTFMatchingResultRequirement {

}
