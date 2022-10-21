package de.upb.crc901.otftestbed.credential_issuer.impl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import de.upb.crc901.otftestbed.commons.config.EnablePocRestTemplate;

@EnablePocRestTemplate
@Configuration
public class CredentialIssuerConfig {

	@Value("${testbed.config.persistence.enabled}")
	private boolean useCachedSystem;

	public static final String ATTRIBUTE_FILENAME = "attributes";

	public static final String CREDENTIAL_ISSUER_FILENAME = "credential_issuer.representation";

	public boolean isPersistent() {
		return useCachedSystem;
	}
}
