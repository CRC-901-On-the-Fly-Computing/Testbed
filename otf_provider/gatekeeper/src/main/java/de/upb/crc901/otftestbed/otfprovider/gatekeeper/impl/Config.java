package de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import de.upb.crc901.otftestbed.commons.config.EnablePocObjectMapper;
import de.upb.crc901.testbed.otfproviderregistry.OTFProvider;

@EnablePocObjectMapper
@Configuration
@PropertySource("classpath:config-${spring.profiles.active}.properties")
public class Config {

    @Value("${otfProviderId}")
    private String otfProviderId;

    @Value("${otfProviderUrl}")
    private String otfProviderUrl;

    @Value("${otfProviderRegistryUrl}")
    private String otfProviderRegistryUrl;

    @Bean
    public OTFProvider getOtfProvider(){
        OTFProvider otfProvider = new OTFProvider(new RemoteOTFPRoviderRegistry(this.otfProviderRegistryUrl), this.otfProviderId, this.otfProviderUrl);
        new Thread(){
            public void run(){
                otfProvider.timeout();
            }
        }.start();
        return otfProvider;
    }
}
