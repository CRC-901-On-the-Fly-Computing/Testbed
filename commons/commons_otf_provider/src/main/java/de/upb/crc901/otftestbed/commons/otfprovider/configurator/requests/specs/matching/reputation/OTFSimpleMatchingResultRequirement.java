package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation;

import io.swagger.annotations.ApiModel;

/**
 * Result requirements for a simple matching result containing only a minimum value.
 *
 * @author jonsen94
 *
 */
@ApiModel(value = "OTFSimpleMatchingResultRequirement", parent = OTFMatchingResultRequirement.class)
public class OTFSimpleMatchingResultRequirement implements OTFMatchingResultRequirement {

  /**
   * Minimum value. Results should be greater or equal to this value.
   */
  private double minValue = 0.5;

  /**
   * Getter for the minimum value.
   *
   * @return Minimum value.
   */
  public double getMinValue() {
    return minValue;
  }

  /**
   * Setter for the minimum value.
   *
   * @param minValue Minimum value that should be used.
   */
  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }


}
