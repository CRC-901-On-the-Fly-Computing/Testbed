package de.upb.crc901.otftestbed.commons.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class URLString {

  @JsonProperty(required = true)
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return url;
  }

}
