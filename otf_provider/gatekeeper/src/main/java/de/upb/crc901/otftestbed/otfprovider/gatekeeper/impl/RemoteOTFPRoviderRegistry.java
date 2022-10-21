package de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.upb.crc901.testbed.otfproviderregistry.OTFProviderRegistryImpl;

public class RemoteOTFPRoviderRegistry extends OTFProviderRegistryImpl {

  public String getOtfProviderRegistryUrl() {
    return otfProviderRegistryUrl;
  }

  private String otfProviderRegistryUrl;

  public RemoteOTFPRoviderRegistry(String url){
    this.otfProviderRegistryUrl = url;
  }

  @Override
  public void receiveMessage(Object m) {
    HttpResponse<String> httpResponse = null;
    try {
      httpResponse = Unirest.post(this.otfProviderRegistryUrl).body(m).asString();
    } catch (UnirestException e) {
      //Hier Log einfügen
    }
    if(httpResponse != null && httpResponse.getStatus() != 200){
      //Hier Log einfügen
    }
  }
}
