package de.upb.crc901.otftestbed.commons.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.UUID;

/**
 * ExecutorDescription
 */
@Validated
public class ExecutorDescription   {
  @JsonProperty("listOfServices")
  private de.upb.crc901.otftestbed.commons.spawner.ListOfServices listOfServices = null;

  @JsonProperty("executorName")
  private UUID executorName = null;

  public ExecutorDescription listOfServices(de.upb.crc901.otftestbed.commons.spawner.ListOfServices listOfServices) {
    this.listOfServices = listOfServices;
    return this;
  }

  /**
   * Get listOfServices
   * @return listOfServices
  **/
  @ApiModelProperty(value = "")

  public de.upb.crc901.otftestbed.commons.spawner.ListOfServices getListOfServices() {
    return listOfServices;
  }

  public void setListOfServices(de.upb.crc901.otftestbed.commons.spawner.ListOfServices listOfServices) {
    this.listOfServices = listOfServices;
  }

  public ExecutorDescription executorName(UUID executorName) {
    this.executorName = executorName;
    return this;
  }

  /**
   * Get executorName
   * @return executorName
  **/
  @ApiModelProperty(value = "")

  public UUID getExecutorName() {
    return executorName;
  }

  public void setExecutorName(UUID executorName) {
    this.executorName = executorName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutorDescription executorDescription = (ExecutorDescription) o;
    return Objects.equals(this.listOfServices, executorDescription.listOfServices) &&
        Objects.equals(this.executorName, executorDescription.executorName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(listOfServices, executorName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutorDescription {\n");

    sb.append("    listOfServices: ").append(toIndentedString(listOfServices)).append("\n");
    sb.append("    executorName: ").append(toIndentedString(executorName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
