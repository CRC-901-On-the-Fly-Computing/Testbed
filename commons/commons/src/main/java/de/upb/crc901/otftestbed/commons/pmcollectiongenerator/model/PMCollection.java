
package de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "description", "order", "folders", "timestamp", "owner", "public",
    "requests"})
public class PMCollection {

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("id")
  private String id;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("name")
  private String name;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("description")
  private String description;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("order")
  private List<String> order = null;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("folders")
  private List<Object> folders = null;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("timestamp")
  private Integer timestamp;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("owner")
  private String owner;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("public")
  private Boolean _public;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("requests")
  private List<Request> requests = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("order")
  public List<String> getOrder() {
    return order;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("order")
  public void setOrder(List<String> order) {
    this.order = order;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("folders")
  public List<Object> getFolders() {
    return folders;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("folders")
  public void setFolders(List<Object> folders) {
    this.folders = folders;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("timestamp")
  public Integer getTimestamp() {
    return timestamp;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("timestamp")
  public void setTimestamp(Integer timestamp) {
    this.timestamp = timestamp;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("owner")
  public String getOwner() {
    return owner;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("owner")
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("public")
  public Boolean getPublic() {
    return _public;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("public")
  public void setPublic(Boolean _public) {
    this._public = _public;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("requests")
  public List<Request> getRequests() {
    return requests;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("requests")
  public void setRequests(List<Request> requests) {
    this.requests = requests;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    additionalProperties.put(name, value);
  }

}
