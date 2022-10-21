package de.upb.crc901.otftestbed.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PocObjectMapperConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		return Helpers.getMapper();
	}
}
