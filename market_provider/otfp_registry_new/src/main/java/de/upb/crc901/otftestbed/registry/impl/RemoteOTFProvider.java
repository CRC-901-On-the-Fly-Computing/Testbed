package de.upb.crc901.otftestbed.registry.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.upb.crc901.testbed.otfproviderregistry.OTFProvider;
import de.upb.crc901.testbed.otfproviderregistry.OTFProviderRegistry;

public class RemoteOTFProvider extends OTFProvider {


    public RemoteOTFProvider(OTFProviderRegistry sup, String id, String url) {
        super(sup, id, url);
    }

    @Override
    public void receiveMessage(Object m) {
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = Unirest.post(getURL()+"/api/otfprovider/receiveMessage").body(m).asString();
        } catch (UnirestException|RuntimeException e) {
            //Hier Log einfügen
        }
        if(httpResponse != null && httpResponse.getStatus() != 200){
            // TODO: 05.12.18 Error loggen Übertragungswiederholung
        }
    }
}
