package de.upb.crc901.otftestbed.commons.otfprovider.matcher.result;

import java.text.DecimalFormat;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "OTFSimpleMatchingResult", parent = OTFMatchingResult.class)
public class OTFSimpleMatchingResult implements OTFMatchingResult {

  private double value = 0.5;

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);

    builder.append("matching res.: " + df.format(value));
    return builder.toString();
  }

}
