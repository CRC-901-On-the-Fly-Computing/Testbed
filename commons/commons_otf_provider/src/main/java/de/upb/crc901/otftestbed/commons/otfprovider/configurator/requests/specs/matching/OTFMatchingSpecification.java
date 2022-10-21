package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching;

import de.upb.crc901.configurationsetting.discovery.MatchingSpecification;

/**
 * Lightweight class wrapping the MatchingSpecification that can be auto serialized and deserialized
 * by Jackson.
 *
 * @author jonsen94
 */
public class OTFMatchingSpecification {

  /** The quality of the matching of the input parameters */
  private Double qualityInputParams = null;
  /** The quality of the matching of the output parameters */
  private Double qualityOutputParams = null;
  /** The quality of the matching of the preconditions */
  private Double qualityPreconditions = null;
  /** The quality of the matching of the effects */
  private Double qualityEffects = null;

  /**
   * Empty constructor so this class can be deserialized this class.
   */
  public OTFMatchingSpecification() {
    // Empty C'tor to make this class deserializable
  }

  /**
   * Basic c'tor to set all fields.
   *
   * @param qualityInputParams
   * @param qualityOutputParams
   * @param qualityPreconditions
   * @param qualityEffects
   */
  public OTFMatchingSpecification(double qualityInputParams, double qualityOutputParams,
      double qualityPreconditions, double qualityEffects) {
    this.qualityInputParams = qualityInputParams;
    this.qualityOutputParams = qualityOutputParams;
    this.qualityPreconditions = qualityPreconditions;
    this.qualityEffects = qualityEffects;
  }

  /**
   * Constructor that copys a {@link MatchingSpecification}.
   *
   * @param matchingSpecification {@link MatchingSpecification} to be copied.
   */
  public OTFMatchingSpecification(MatchingSpecification matchingSpecification) {
    qualityInputParams = matchingSpecification.getqI();
    qualityOutputParams = matchingSpecification.getqO();
    qualityPreconditions = matchingSpecification.getqP();
    qualityEffects = matchingSpecification.getqE();
  }

  public double getQualityInputParams() {
    return qualityInputParams;
  }

  public void setQualityInputParams(double qualityInputParams) {
    this.qualityInputParams = qualityInputParams;
  }

  public double getQualityOutputParams() {
    return qualityOutputParams;
  }

  public void setQualityOutputParams(double qualityOutputParams) {
    this.qualityOutputParams = qualityOutputParams;
  }

  public double getQualityPreconditions() {
    return qualityPreconditions;
  }

  public void setQualityPreconditions(double qualityPreconditions) {
    this.qualityPreconditions = qualityPreconditions;
  }

  public double getQualityEffects() {
    return qualityEffects;
  }

  public void setQualityEffects(double qualityEffects) {
    this.qualityEffects = qualityEffects;
  }

  /**
   * Generates a {@link MatchingSpecification}.
   *
   * @return Generated {@link MatchingSpecification}.
   */
  public MatchingSpecification generateMatchingSpecification() {
    // We do not want to set null as any param so make it 0 instead.
    double tempInputParams = (qualityInputParams == null) ? 0 : qualityInputParams;
    double tempOutputParams = (qualityInputParams == null) ? 0 : qualityInputParams;
    double tempPreconditions = (qualityInputParams == null) ? 0 : qualityInputParams;
    double tempEffects = (qualityInputParams == null) ? 0 : qualityInputParams;


    if ((qualityInputParams == null) && (qualityOutputParams == null)) {
      if (qualityPreconditions == null) {
        return new MatchingSpecification(tempEffects);
      }
      return new MatchingSpecification(tempPreconditions, tempEffects);
    }
    return new MatchingSpecification(tempInputParams, tempOutputParams, tempPreconditions,
        tempEffects);
  }

  /**
   * Uses the {@link MatchingSpecification}s toString methode to generate a string.
   */
  @Override
  public String toString() {
    return generateMatchingSpecification().toString();
  }

}
