package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.sse.reputation_request.ReputationRequest;
import de.upb.crc901.sse.reputation_request.Reputation_requestFactory;

/**
 * A reputation request used to match a service.
 *
 * @author jonsen94
 *
 */
public class OTFReputationRequest {

  /**
   * List of {@link OTFReputationCondition}s of this request. May not be empty.
   */
  @JsonProperty(required = true)
  private List<OTFReputationCondition> reputationConditions;

  /**
   * Result requirements for this request.
   */
  private OTFMatchingResultRequirement matchingResultReq;

  
  /**
   * Setter for the list containing reputation conditions.
   */
  public void setReputationConditions(List<OTFReputationCondition> reputationConditions) {
   this.reputationConditions = reputationConditions;
  }
  /**
   * Getter for the list containing reputation conditions.
   *
   * @return Reputation condition list. If list is null it will be created.
   */
  public List<OTFReputationCondition> getReputationConditions() {
    if (reputationConditions == null) {
      reputationConditions = new ArrayList<>();
    }

    return reputationConditions;
  }

  /**
   * Generates a {@link ReputationRequest} from this class.
   *
   * @return A generated reputation request.
   */
  public ReputationRequest generateReputationRequest() {
    ReputationRequest repRequest = Reputation_requestFactory.eINSTANCE.createReputationRequest();
    for (OTFReputationCondition condition : reputationConditions) {
      repRequest.getConditions().add(condition.generateReputationCondition());
    }
    return repRequest;
  }

  /**
   * Getter for the matching result requirements.
   *
   * @return OTFMatchingResultRequirement.
   */
  public OTFMatchingResultRequirement getMatchingResultReq() {
    return matchingResultReq;
  }

  /**
   * Setter for the matching result requirements
   *
   * @param matchingResultReq Requirements to be set.
   */
  public void setMatchingResultReq(OTFMatchingResultRequirement matchingResultReq) {
    this.matchingResultReq = matchingResultReq;
  }

  /**
   * Checks if all needed fields are set correctly.
   *
   * @return False if not all fields are set correctly. True otherwise.
   */
  public boolean allRelevantFieldsSet() {
    if (matchingResultReq == null) {
      return false;
    }
    for (OTFReputationCondition condition : reputationConditions) {
      if (!condition.allFieldsSet()) {
        return false;
      }
    }
    return true;
  }

}
