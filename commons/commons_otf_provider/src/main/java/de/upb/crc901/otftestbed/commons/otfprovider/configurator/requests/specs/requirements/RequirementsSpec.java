package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.basicconfigurator.core.phase.ExplorationStrategy;
import de.upb.crc901.configurationsetting.logic.Literal;
import de.upb.crc901.configurationsetting.logic.VariableParam;
import de.upb.crc901.configurationsetting.nonfunctionalrequirements.NonFunctionalRequirementVector;
import de.upb.crc901.configurationsetting.operation.Operation;
import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.OTFMatchingSpecification;
import de.upb.crc901.otftestbed.commons.otfprovider.requirementspec.SimpleRequirementsSpec;

/**
 * Requirements specification for a configurator.
 *
 * @author jonsen94
 *
 */
public class RequirementsSpec extends SimpleRequirementsSpec {

  // Configurator

  /**
   * Logger for this class.
   */
  private static final Logger log = LoggerFactory.getLogger(RequirementsSpec.class);

  /**
   * Callback URL that should be called when the request is finished
   */
  @JsonProperty(required = true)
  private String callbackURL = "";

  /**
   * Timeout for the request.
   */
  @JsonProperty(required = true)
  private long timeout = -1;

  /**
   * Matching specification for the request.
   */
  @JsonProperty(required = true)
  private OTFMatchingSpecification matchingSpecification = null;

  /**
   * Exploration strategy that should be used.
   */
  @JsonProperty(required = true)
  private ExplorationStrategy strategy = null;

  /**
   * Empty C'tor needed for deserialization.
   */
  public RequirementsSpec() {
    // Empty constructor needed for json deserialization
  }
  
  /**
   * Extend C'tor to copy values from a {@link SimpleRequirementsSpec}.
   */
  public RequirementsSpec(SimpleRequirementsSpec simpleSpec) {
    setRequester(simpleSpec.getRequester());
    getPreCondition().addAll(simpleSpec.getPreCondition());
    getEffect().addAll(simpleSpec.getEffect());
    getInputParams().addAll(simpleSpec.getInputParams());
    getOutputParams().addAll(simpleSpec.getOutputParams());
    setNonFunctionalRequirements(simpleSpec.getNonFunctionalRequirements());
    setOperationName(simpleSpec.getOperationName());
    getReputationRequest().addAll(simpleSpec.getReputationRequest());
  }

  /**
   * Loads fields from an {@link Operation}.
   *
   * @param op Operation to be loaded. If it's null no fields will be set.
   */
  public void loadOperation(Operation op) {
    if (op == null) {
      log.warn("Null operation to laod from. Nothing loaded.");
      return;
    }

    operationName = op.getName();

    inputParams = new ArrayList<>();
    for (VariableParam param : op.getInputParameters()) {
      RequirementsSpecParameter reqParam = new RequirementsSpecParameter(param);
      inputParams.add(reqParam);
    }

    outputParams = new ArrayList<>();
    for (VariableParam param : op.getOutputParameters()) {
      RequirementsSpecParameter reqParam = new RequirementsSpecParameter(param);
      outputParams.add(reqParam);
    }

    preCondition = new ArrayList<>();
    for (Literal literal : op.getPrecondition().getCondition()) {
      RequirementsSpecLiteral reqLiteral = new RequirementsSpecLiteral(literal);
      preCondition.add(reqLiteral);
    }

    effect = new ArrayList<>();
    for (Literal literal : op.getEffect().getCondition()) {
      RequirementsSpecLiteral reqLiteral = new RequirementsSpecLiteral(literal);
      effect.add(reqLiteral);
    }

    NonFunctionalRequirementVector vector = op.getNonFunctionalRequirements();
    vector = vector.clone();
    nonFunctionalRequirements = new Number[vector.getLength()];
    for (int i = 0; i < vector.getLength(); i++) {
      nonFunctionalRequirements[i] = vector.getValue(i);
    }
  }

  /**
   * Generates an operation string that than can be used to generate an operation with a specific
   * composition domain.
   *
   * @return String that can be loaded as a operation.
   */
  public String generateOperationString() {
    StringBuilder operationString = new StringBuilder();
    operationString.append(operationName);
    operationString.append(";");

    requirementsSpecParamOperationString(operationString, getInputParams());

    requirementsSpecParamOperationString(operationString, getOutputParams());

    requirementsSpecLiteralOperationString(operationString, getPreCondition());

    requirementsSpecLiteralOperationString(operationString, getEffect());

    operationString.append("(");
    for (Number nonFunctional : nonFunctionalRequirements) {
      operationString.append(nonFunctional.toString());
      operationString.append(",");
    }
    if (nonFunctionalRequirements.length > 0) {
      operationString.deleteCharAt(operationString.length() - 1);
    }
    operationString.append(")");

    return operationString.toString();
  }

  private void requirementsSpecLiteralOperationString(StringBuilder operationString,
      List<RequirementsSpecLiteral> literals) {
    for (RequirementsSpecLiteral literal : literals) {
      operationString.append(literal.convertForOperationString());
      operationString.append("&");
    }
    if (!literals.isEmpty()) {
      operationString.deleteCharAt(operationString.length() - 1);
    }
    operationString.append(";");
  }

  private void requirementsSpecParamOperationString(StringBuilder operationString,
      List<RequirementsSpecParameter> params) {
    for (RequirementsSpecParameter input : params) {
      operationString.append(input.getName());
      operationString.append(":");
      operationString.append(input.getDatatype());
      operationString.append(",");
    }
    if (!params.isEmpty()) {
      operationString.deleteCharAt(operationString.length() - 1);
    }
    operationString.append(";");
  }

  public long getTimeout() {
    return timeout;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public OTFMatchingSpecification getMatchingSpecification() {
    return matchingSpecification;
  }

  public void setMatchingSpecification(OTFMatchingSpecification matchingSpecification) {
    this.matchingSpecification = matchingSpecification;
  }

  public ExplorationStrategy getStrategy() {
    return strategy;
  }

  public void setStrategy(ExplorationStrategy strategy) {
    this.strategy = strategy;
  }

  /**
   * Checks whether all fields are not null and timeout is greater or equal to zero.
   *
   * @return True if all fields are set, false otherwise.
   */
  @Override
  public boolean allFieldsSet() {
    if (!super.allFieldsSet()) {
      return false;
    }
    if (callbackURL == null) {
      return false;
    }
    if (timeout < 0) {
      return false;
    }
    if (matchingSpecification == null) {
      return false;
    }
    if (strategy == null) {
      return false;
    }
    return true;
  }

  public String getCallbackURL() {
    return callbackURL;
  }

  public void setCallbackURL(String callbackURL) {
    this.callbackURL = callbackURL;
  }

  @Override
  public String toString() {
    return "RequirementsSpec [callbackURL=" + callbackURL + ", timeout=" + timeout
        + ", matchingSpecification=" + matchingSpecification + ", strategy=" + strategy
        + ", operationName=" + operationName + ", inputParams=" + inputParams + ", outputParams="
        + outputParams + ", preCondition=" + preCondition + ", effect=" + effect
        + ", nonFunctionalRequirements=" + Arrays.toString(nonFunctionalRequirements)
        + ", requester=" + requester + ", reputationRequest=" + reputationRequest + "]";
  }

}
