package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements;

import de.upb.crc901.configurationsetting.logic.VariableParam;

public class RequirementsSpecParameter {

  private String name = "";
  private String datatype = "";

  public RequirementsSpecParameter() {
    // Empty constructor needed for json deserialization
  }

  public RequirementsSpecParameter(String name, String datatype) {
    this.name = name;
    this.datatype = datatype;
  }

  public RequirementsSpecParameter(VariableParam param) {
    name = param.getName();
    datatype = param.getType().getName();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDatatype() {
    return datatype;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }

}
