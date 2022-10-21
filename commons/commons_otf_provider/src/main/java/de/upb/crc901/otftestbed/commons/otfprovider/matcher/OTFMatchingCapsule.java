package de.upb.crc901.otftestbed.commons.otfprovider.matcher;

import java.util.ArrayList;
import java.util.List;

import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation.OTFReputationCondition;

public class OTFMatchingCapsule {

  private List<String> services;
  private List<OTFReputationCondition> conditions;

  public OTFMatchingCapsule() {
    services = new ArrayList<>();
  }

  public List<String> getServices() {
    return services;
  }

  public void setServices(List<String> services) {
    this.services = services;
  }

public List<OTFReputationCondition> getConditions() {
	return conditions;
}

public void setConditions(List<OTFReputationCondition> conditions) {
	this.conditions = conditions;
}


}
