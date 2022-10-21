package de.upb.crc901.otftestbed.commons.connect;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "de.upb.crc901.otftestbed.commons.connect")
@Import(org.springframework.web.client.RestTemplate.class)
public class PocConnectorConfiguration {

}
