package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.sse.reputation_request.FiveStarRange;
import de.upb.crc901.sse.reputation_request.Operators;
import de.upb.crc901.sse.reputation_request.ReputationCondition;
import de.upb.crc901.sse.reputation_request.Reputation_requestFactory;
import de.upb.crc901.sse.reputation_request.RequestedFiveStarValue;
import de.upb.crc901.sse.reputation_request.ServiceContext;
import de.upb.crc901.sse.reputation_request.ServiceContextKind;

/**
 * JSon serializable class containing important informations.
 *
 * @see ReputationCondition
 * @author jonsen94
 *
 */
public class OTFReputationCondition {

  /**
   * Testbed context for this condition.
   */
  @JsonProperty(required = true)
  private OTFContext context = null;

  /**
   * {@link FiveStarRange} value for this condition.
   */
  @JsonProperty(required = true)
  private FiveStarRange requestedFiveStarRange = null;

  /**
   * Operator for this condition.
   */
  @JsonProperty(defaultValue = "GREATER")
  private Operators operator = Operators.GREATER;

  /**
   * Getter for the context.
   *
   * @return Context of this condition.
   */
  public OTFContext getContext() {
    return context;
  }

  /**
   * Setter for this conditions context.
   *
   * @param context Context to be used in this condition.
   */
  public void setContext(OTFContext context) {
    this.context = context;
  }

  /**
   * Getter for the five star range.
   *
   * @return Five star range of this condition.
   */
  public FiveStarRange getRequestedFiveStarRange() {
    return requestedFiveStarRange;
  }

  /**
   * Setter for this conditions five star range.
   *
   * @param requestedFiveStarValue Five star range to be used in this condition.
   */
  public void setRequestedFiveStarRange(FiveStarRange requestedFiveStarValue) {
    requestedFiveStarRange = requestedFiveStarValue;
  }

  /**
   * Getter for the operator.
   *
   * @return Operator of this condition.
   */
  public Operators getOperator() {
    return operator;
  }

  /**
   * Setter for the operator.
   *
   * @param operator Operator to be used in this condition.
   */
  public void setOperator(Operators operator) {
    this.operator = operator;
  }

  /**
   * Generates a {@link ReputationCondition} from this objects data.
   *
   * @return null if any of this objects fields is null. A {@link ReputationCondition} filled with
   *         this objects data otherwise.
   */
  public ReputationCondition generateReputationCondition() {
    if (!allFieldsSet()) {
      return null;
    }

    ReputationCondition reputationCondition =
        Reputation_requestFactory.eINSTANCE.createReputationCondition();


    ServiceContext tempContext = Reputation_requestFactory.eINSTANCE.createServiceContext();
    tempContext.setServiceContext(ServiceContextKind.getByName(context.name()));
    reputationCondition.setContext(tempContext);


    RequestedFiveStarValue tempRequestedValue =
        Reputation_requestFactory.eINSTANCE.createRequestedFiveStarValue();
    tempRequestedValue.setValue(requestedFiveStarRange);
    reputationCondition.setRequestedValue(tempRequestedValue);


    reputationCondition.setOperator(operator);

    return reputationCondition;
  }

  /**
   * Checks if all fields are set.
   *
   * @return False if at least one field is null. True otherwise.
   */
  public boolean allFieldsSet() {
    if (context == null) {
      return false;
    }
    if (requestedFiveStarRange == null) {
      return false;
    }
    if (operator == null) {
      return false;
    }
    return true;
  }
}
