package de.upb.crc901.otftestbed.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnablePocObjectMapper
@Configuration
public class PocRestTemplateConfiguration {

	@Autowired
	private ObjectMapper objectMapper;


	@Bean
	public RestTemplate restTemplate() {
		// list of added converters
		Map<Class<? extends HttpMessageConverter<?>>, HttpMessageConverter<?>> converters = new HashMap<>();

		// this converters are added to the converters list.
		// existing converters of same type are replaced.
		converters.put(MappingJackson2HttpMessageConverter.class, mappingJackson2HttpMessageConverter());
		converters.put(StringHttpMessageConverter.class, stringHttpMessageConverter());

		// create RestTemplate with default converters
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

		// remove default converters of same type as the added ones
		messageConverters.removeIf(c -> converters.containsKey(c.getClass()));

		// add new converters
		messageConverters.addAll(converters.values());

		return restTemplate;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}
}
