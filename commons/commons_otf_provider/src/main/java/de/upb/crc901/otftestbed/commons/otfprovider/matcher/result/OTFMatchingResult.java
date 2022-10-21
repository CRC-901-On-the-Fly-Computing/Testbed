package de.upb.crc901.otftestbed.commons.otfprovider.matcher.result;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.annotations.ApiModel;

/**
 * Superclass of all matching results.
 *
 * @author jonsen94
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OTFFuzzyMatchingResult.class, name = "OTFFuzzyMatchingResult"),
    @JsonSubTypes.Type(value = OTFSimpleMatchingResult.class, name = "OTFSimpleMatchingResult")})
@ApiModel(value = "OTFMatchingResult",
    subTypes = {OTFSimpleMatchingResult.class, OTFFuzzyMatchingResult.class},
    discriminator = "type")
public interface OTFMatchingResult {
  // intentionally left empty
}
