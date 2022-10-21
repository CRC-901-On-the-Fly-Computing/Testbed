package de.upb.crc901.otftestbed.commons.flow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import de.upb.crc901.otftestbed.commons.flow.schema.Flow;
import de.upb.crc901.otftestbed.commons.service_specification.schema.ServiceSpecification;

// Use to store and load from db
// TODO: documentation
public class ExtendedFlow {

  private Flow flow;
  private UUID uuid;
  private List<ServiceSpecification> serviceSpecifications;
  private Date creationDate;
  private List<String> ontologies;
  private List<String> ontologieRules;
  private boolean refreshOntologies;
  private boolean refreshOntologieRules;

  public ExtendedFlow() {
    serviceSpecifications = new ArrayList<>();
    creationDate = Calendar.getInstance().getTime();
    ontologies = new ArrayList<>();
    ontologieRules = new ArrayList<>();
    refreshOntologieRules = true;
    refreshOntologies = true;
  }

  public List<ServiceSpecification> getServiceSpecifications() {
    return serviceSpecifications;
  }

  public void setServiceSpecifications(List<ServiceSpecification> serviceSpecs) {
    serviceSpecifications = serviceSpecs;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public Flow getFlow() {
    return flow;
  }

  public void setFlow(Flow flow) {
    this.flow = flow;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public List<String> getOntologies() {
    return ontologies;
  }

  public void setOntologies(List<String> ontologies) {
    this.ontologies = ontologies;
  }

public List<String> getOntologieRules() {
	return ontologieRules;
}

public void setOntologieRules(List<String> ontologieRules) {
	this.ontologieRules = ontologieRules;
}

public boolean isRefreshOntologies() {
	return refreshOntologies;
}

public void setRefreshOntologies(boolean refreshOntologies) {
	this.refreshOntologies = refreshOntologies;
}

public boolean isRefreshOntologieRules() {
	return refreshOntologieRules;
}

public void setRefreshOntologieRules(boolean refreshOntologieRules) {
	this.refreshOntologieRules = refreshOntologieRules;
}


}
