package de.upb.crc901.otftestbed.review_board.impl.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.upb.crc901.otftestbed.commons.config.EnablePocRestTemplate;
import de.upb.crc901.otftestbed.review_board.impl.repositories.SearchableSignedReputationReadConverter;
import de.upb.crc901.otftestbed.review_board.impl.repositories.SearchableSignedReputationWriteConverter;

@EnablePocRestTemplate
@Configuration
public class Config {

	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(new SearchableSignedReputationReadConverter());
		converters.add(new SearchableSignedReputationWriteConverter());
		return new MongoCustomConversions(converters);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("PUT", "DELETE", "POST", "GET");
			}
		};
	}
}
