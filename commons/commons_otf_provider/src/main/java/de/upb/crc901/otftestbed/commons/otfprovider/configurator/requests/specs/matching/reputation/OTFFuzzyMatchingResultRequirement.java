package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation;

import io.swagger.annotations.ApiModel;

/**
 * Requirements for a matching result.
 *
 * @author jonsen94
 *
 */
@ApiModel(value = "OTFFuzzyMatchingResultRequirement", parent = OTFMatchingResultRequirement.class)
public class OTFFuzzyMatchingResultRequirement implements OTFMatchingResultRequirement {

  /**
   * Minimum lower bound thats needed to accept a result.
   */
  private double minLowerBound = 0;

  /**
   * Minimum upper bound thats needed to accept a result.
   */
  private double minUpperBound = 0;

  /**
   * Getter for the minimum lower bound.
   *
   * @return Minimum lower bound.
   */
  public double getMinLowerBound() {
    return minLowerBound;
  }

  /**
   * Setter for the minimum lower bound.
   *
   * @param minLowerBound Minimum lower bound for these fuzzy result requirements.
   */
  public void setMinLowerBound(double minLowerBound) {
    this.minLowerBound = minLowerBound;
  }

  /**
   * Getter for the minimum upper bound.
   *
   * @return Minimum upper bound.
   */
  public double getMinUpperBound() {
    return minUpperBound;
  }

  /**
   * Setter for the minimum upper bound.
   *
   * @param minUpperBound Minimum upper bound for these fuzzy result requirements.
   */
  public void setMinUpperBound(double minUpperBound) {
    this.minUpperBound = minUpperBound;
  }
}
