package de.upb.crc901.otftestbed.commons.reputation;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceReputationList {

  private int size = 0;
  private String name = "";
  private List<ServiceReputation> serviceReputation;

  /**
   * Empty constructor so this class can be deserialized from json.
   */
  public ServiceReputationList() {
    // Empty constructor to make this class compatible with Jacksons json deserializer.
  }

  public List<ServiceReputation> getServiceReputation() {
    if (serviceReputation == null) {
      serviceReputation = new ArrayList<>();
    }
    return serviceReputation;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
