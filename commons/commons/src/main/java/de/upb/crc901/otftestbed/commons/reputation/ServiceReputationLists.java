package de.upb.crc901.otftestbed.commons.reputation;

import java.util.ArrayList;
import java.util.List;

public class ServiceReputationLists extends ServiceReputationList {

  private List<ServiceReputationList> serviceReputationListing;

  public ServiceReputationLists() {
    // Empty constructor to make this class compatible with Jacksons json deserializer.
  }

  public List<ServiceReputationList> getServiceReputationLists() {
    if (serviceReputationListing == null) {
      serviceReputationListing = new ArrayList<>();
    }
    return serviceReputationListing;
  }
}
