package de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements;

import java.util.ArrayList;
import java.util.List;

import de.upb.crc901.configurationsetting.logic.Literal;
import de.upb.crc901.configurationsetting.logic.LiteralParam;

public class RequirementsSpecLiteral {

  private String name = "";

  private List<String> params = new ArrayList<>();

  public RequirementsSpecLiteral() {
    // Empty constructor needed for json deserialization
  }

  public RequirementsSpecLiteral(String name, List<String> params) {
    this.name = name;
    if (params != null) {
      this.params = params;
    }
  }

  public RequirementsSpecLiteral(Literal literal) {
    name = literal.getProperty();
    for (LiteralParam param : literal.getParameters()) {
      params.add(param.getName());
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getParams() {
    if (params == null) {
      params = new ArrayList<>();
    }
    return params;
  }

  public String convertForOperationString() {
    StringBuilder literalString = new StringBuilder();
    literalString.append(name);
    literalString.append("(");
    for (String param : params) {
      literalString.append(param);
      literalString.append(",");
    }
    literalString.deleteCharAt(literalString.length() - 1);

    literalString.append(")");

    return literalString.toString();
  }

}
