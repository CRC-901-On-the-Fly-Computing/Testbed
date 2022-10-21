package de.upb.crc901.otftestbed.commons.otfprovider.matcher.result;

import java.text.DecimalFormat;

import io.swagger.annotations.ApiModel;

/**
 * MatchingResult class to be send after the matching process.
 *
 * @author jonsen94
 *
 */
@ApiModel(value = "OTFFuzzyMatchingResult", parent = OTFMatchingResult.class)
public class OTFFuzzyMatchingResult implements OTFMatchingResult {

  /**
   * Lower threshold of the matching result.
   */
  private double lowerThreshold = 0;

  /**
   * Upper threshold of the matching result.
   */
  private double upperThreshold = 0;


  public double getLowerThreshold() {
    return lowerThreshold;
  }

  public void setLowerThreshold(double lowerThreshold) {
    this.lowerThreshold = lowerThreshold;
  }

  public double getUpperThreshold() {
    return upperThreshold;
  }

  public void setUpperThreshold(double upperThreshold) {
    this.upperThreshold = upperThreshold;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);

    builder.append("lowerBound:" + df.format(lowerThreshold));
    builder.append("; ");
    builder.append("upperBound:" + df.format(upperThreshold));
    return builder.toString();
  }

}
