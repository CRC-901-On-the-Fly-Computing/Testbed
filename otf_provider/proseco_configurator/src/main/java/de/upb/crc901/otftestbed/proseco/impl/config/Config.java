package de.upb.crc901.otftestbed.proseco.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.upb.crc901.otftestbed.commons.config.EnablePocRestTemplate;

@EnablePocRestTemplate
@EnableScheduling
@Configuration
public class Config {
}
