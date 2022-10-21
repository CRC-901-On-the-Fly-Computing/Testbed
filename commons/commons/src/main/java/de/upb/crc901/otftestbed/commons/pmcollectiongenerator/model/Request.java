
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
@JsonPropertyOrder({"id", "headers", "url", "preRequestScript", "pathVariables", "method", "data",
    "dataMode", "version", "tests", "currentHelper", "helperAttributes", "time", "name",
    "description", "collectionId", "responses", "rawModeData"})
public class Request {

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
  @JsonProperty("headers")
  private String headers;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("url")
  private String url;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("preRequestScript")
  private Object preRequestScript;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("pathVariables")
  private PathVariables pathVariables;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("method")
  private String method;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("data")
  private List<Object> data = null;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("dataMode")
  private String dataMode;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("version")
  private Integer version;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("tests")
  private Object tests;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("currentHelper")
  private String currentHelper;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("helperAttributes")
  private HelperAttributes helperAttributes;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("time")
  private Integer time;
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
  @JsonProperty("collectionId")
  private String collectionId;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("responses")
  private List<Object> responses = null;
  /**
   *
   * (Required)
   *
   */
  @JsonProperty("rawModeData")
  private String rawModeData;
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
  @JsonProperty("headers")
  public String getHeaders() {
    return headers;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("headers")
  public void setHeaders(String headers) {
    this.headers = headers;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("preRequestScript")
  public Object getPreRequestScript() {
    return preRequestScript;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("preRequestScript")
  public void setPreRequestScript(Object preRequestScript) {
    this.preRequestScript = preRequestScript;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("pathVariables")
  public PathVariables getPathVariables() {
    return pathVariables;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("pathVariables")
  public void setPathVariables(PathVariables pathVariables) {
    this.pathVariables = pathVariables;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("method")
  public String getMethod() {
    return method;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("method")
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("data")
  public List<Object> getData() {
    return data;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("data")
  public void setData(List<Object> data) {
    this.data = data;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("dataMode")
  public String getDataMode() {
    return dataMode;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("dataMode")
  public void setDataMode(String dataMode) {
    this.dataMode = dataMode;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("version")
  public Integer getVersion() {
    return version;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("version")
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("tests")
  public Object getTests() {
    return tests;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("tests")
  public void setTests(Object tests) {
    this.tests = tests;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("currentHelper")
  public String getCurrentHelper() {
    return currentHelper;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("currentHelper")
  public void setCurrentHelper(String currentHelper) {
    this.currentHelper = currentHelper;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("helperAttributes")
  public HelperAttributes getHelperAttributes() {
    return helperAttributes;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("helperAttributes")
  public void setHelperAttributes(HelperAttributes helperAttributes) {
    this.helperAttributes = helperAttributes;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("time")
  public Integer getTime() {
    return time;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("time")
  public void setTime(Integer time) {
    this.time = time;
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
  @JsonProperty("collectionId")
  public String getCollectionId() {
    return collectionId;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("collectionId")
  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("responses")
  public List<Object> getResponses() {
    return responses;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("responses")
  public void setResponses(List<Object> responses) {
    this.responses = responses;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("rawModeData")
  public String getRawModeData() {
    return rawModeData;
  }

  /**
   *
   * (Required)
   *
   */
  @JsonProperty("rawModeData")
  public void setRawModeData(String rawModeData) {
    this.rawModeData = rawModeData;
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
