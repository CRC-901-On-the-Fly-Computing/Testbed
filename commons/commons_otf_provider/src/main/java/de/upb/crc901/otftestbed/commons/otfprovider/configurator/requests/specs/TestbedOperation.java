package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs;

import de.upb.crc901.configurationsetting.nonfunctionalrequirements.NonFunctionalRequirementVector;
import de.upb.crc901.configurationsetting.operation.ConfigurationState;
import de.upb.crc901.configurationsetting.operation.Operation;
import de.upb.crc901.otftestbed.commons.otfprovider.matcher.result.OTFMatchingResult;

/**
 * TestbedOperation is used to store a matching result inside an operation the configurator can work
 * with.
 *
 * @author jonsen94
 *
 */
public class TestbedOperation extends Operation {

  /**
   * Matching result to be stored.
   */
  private OTFMatchingResult matchingResult = null;

  /**
   * @see Operation
   */
  public TestbedOperation(String name, ConfigurationState pRequiredInterface,
      ConfigurationState pProvidedInterface,
      NonFunctionalRequirementVector nonFunctionRequirementVector) {
    super(name, pRequiredInterface, pProvidedInterface, nonFunctionRequirementVector);
  }

  /**
   * Loads an operation.
   *
   * @param operation Operation to be loaded.
   */
  public TestbedOperation(Operation operation) {
    super(operation.getName(), operation.getPrecondition(), operation.getEffect(),
        operation.getNonFunctionalRequirements());
  }

  /**
   * Getter for the matching result.
   *
   * @return Stored matching result. May be null.
   */
  public OTFMatchingResult getMatchingResult() {
    return matchingResult;
  }

  /**
   * Setter for the matching result.
   *
   * @param matchingResult Matching result for this operation.
   */
  public void setMatchingResult(OTFMatchingResult matchingResult) {
    this.matchingResult = matchingResult;
  }

}
