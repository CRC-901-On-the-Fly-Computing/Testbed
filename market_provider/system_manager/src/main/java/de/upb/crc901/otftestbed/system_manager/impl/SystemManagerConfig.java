package de.upb.crc901.otftestbed.system_manager.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import de.upb.crc901.otftestbed.commons.config.EnablePocObjectMapper;

@EnablePocObjectMapper
@Configuration
public class SystemManagerConfig {

	@Value("${testbed.config.persistence.enabled}")
	private boolean useCachedSystem;

	public static final String SYSMANAGER_FILENAME = "system_manager.representation";

	public static final String PUBLIC_PARAMETERS_FILENAME = "public_parameters.representation";

	public boolean isPersistent() {
		return useCachedSystem;
	}
}
