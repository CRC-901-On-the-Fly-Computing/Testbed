package de.upb.crc901.otftestbed.registry.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonCreator;

import de.upb.crc901.otftestbed.commons.config.EnablePocObjectMapper;
import de.upb.crc901.testbed.otfproviderregistry.OTFProviderRegistryImpl;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;

@EnablePocObjectMapper
@Configuration
public class Config {

    @JsonCreator
    public static MessageType forValue(String value) {
        return MessageType.valueOf(value);
    }

    @Bean
    public OTFProviderRegistryImpl getRegistry(){
         OTFProviderRegistryImpl registry = new OTFProviderRegistryImpl();
         new Thread(){
             public void run(){
                 registry.timeout();
             }
         }.start();
         return registry;
     }
}
