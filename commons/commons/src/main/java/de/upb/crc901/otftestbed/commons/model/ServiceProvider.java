package de.upb.crc901.otftestbed.commons.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.web.client.RestClientException;

import de.upb.crc901.otftestbed.postgrest.generated.java_client.api.CodeProviderRegistryApi;
import de.upb.crc901.otftestbed.postgrest.generated.java_client.model.CodeProviderRegistry;

/**
 * The representation of service providers.
 *
 * @author Roman
 */
public class ServiceProvider {

	/**
	 * The logger.
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ServiceProvider.class);

	/**
	 * The api to the provider DB.
	 */
	private static final CodeProviderRegistryApi CODE_PROVIDER_REGISTRY_API = new CodeProviderRegistryApi();

	/**
	 * A map to cache the fetched providers.
	 */
	private static final Map<String, ServiceProvider> providers = new HashMap<>();


	/**
	 * The id of the provider.
	 */
	private final String id;


	/**
	 * Constructs a {@link ServiceProvider} with the given id.
	 *
	 * @param id - of the provider
	 */
	private ServiceProvider(String id) {
		this.id = id;
	}


	/**
	 * Gets {@link ServiceProvider} with the given id.
	 *
	 * @param id - of the provider
	 * @return an instance of the {@link ServiceProvider}
	 */
	public static ServiceProvider withId(String id) {
		return providers.getOrDefault(id, create(id));
	}

	/**
	 * Creates a {@link ServiceProvider} instance with the given id.
	 * The creation is thread-safe.
	 *
	 * @param id - of the provider
	 * @return an instance of the {@link ServiceProvider}
	 */
	private static synchronized ServiceProvider create(String id) {
		return providers.computeIfAbsent(id, ServiceProvider::new);
	}

	/**
	 * Requests the list of {@link ServiceProvider}s from DB.
	 *
	 * @return the list of {@link ServiceProvider}s or an empty list on error
	 */
	public static List<ServiceProvider> getAll() {
		List<CodeProviderRegistry> response;

		try {
			response = CODE_PROVIDER_REGISTRY_API.codeProviderRegistryGet(null, null, null, null, "code_provider_id", null, null, null, null, null, null);
		} catch (RestClientException e) {
			log.error("Error fetching list of providers.\n{}", e);
			return Collections.emptyList();
		}

		List<ServiceProvider> providerList = new ArrayList<>();

		for (CodeProviderRegistry cpr : response) {
			providerList.add(ServiceProvider.withId(cpr.getCodeProviderId()));
		}

		return providerList;
	}


	/**
	 * Gets the id of the provider.
	 *
	 * @return the id of the provider
	 */
	public String getId() {
		return id;
	}
}
