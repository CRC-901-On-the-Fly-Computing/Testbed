package de.upb.crc901.otftestbed.kubernetes_spawner.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.upb.crc901.otftestbed.commons.config.EnablePocObjectMapper;

@EnablePocObjectMapper
@Configuration
public class Config {

	public static final String SEDE_PATH = "/sede";
	public static final String SEDE_BACKUP_PATH = "/sede_backup";
	public static final String SEDE_INSTANCES_PATH = "/sede_instances";

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
