package de.upb.crc901.otftestbed.buy_processor.impl.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import de.upb.crc901.otftestbed.buy_processor.impl.repositories.StoredOfferReadConverter;
import de.upb.crc901.otftestbed.buy_processor.impl.repositories.StoredOfferWriteConverter;
import de.upb.crc901.otftestbed.commons.config.EnablePocRestTemplate;

@EnablePocRestTemplate
@Configuration
public class BuyProcessorConfig {

	@Value("${testbed_kubernetes.host}")
	private String kubernetesHost;

	@Value("${testbed.config.persistence.enabled}")
	private boolean useCachedSystem;

	public static final String REVIEW_TOKEN_ISSUER_FILENAME = "review_token_issuer.representation";

	public static final String ACCESS_TOKEN_ISSUER_FILENAME = "access_token_issuer.representation";

	public boolean isPersistent() {
		return useCachedSystem;
	}

	public String getKubernetesHost(int port) {
		StringBuilder sb = new StringBuilder();
		sb.append(kubernetesHost).append(":").append(Integer.toString(port));
		return sb.toString();
	}

	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(new StoredOfferReadConverter());
		converters.add(new StoredOfferWriteConverter());
		return new MongoCustomConversions(converters);
	}
}
