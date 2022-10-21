package de.upb.crc901.otftestbed.commons.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.web.client.RestClientException;

import de.upb.crc901.otftestbed.postgrest.generated.java_client.api.ServiceRegistryApi;
import de.upb.crc901.otftestbed.postgrest.generated.java_client.model.ServiceRegistry;

/**
 * The representation of services.
 *
 * @author Roman
 */
public class Service {

	/**
	 * The logger.
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(Service.class);

	/**
	 * The api to the service DB.
	 */
	private static final ServiceRegistryApi SERVICE_REGISTRY_API = new ServiceRegistryApi();

	/**
	 * A map to cache the fetched services.
	 */
	private static final Map<String, Service> services = new HashMap<>();


	/**
	 * The id of the service.
	 */
	private final String id;

	/**
	 * The {@link ServiceProvider} of the service.
	 */
	private ServiceProvider serviceProvider = null;

	/**
	 * The price of the service.
	 */
	private Double price = null;


	/**
	 * Constructs a {@link Service} with the given id.
	 *
	 * @param id - of the service
	 */
	private Service(String id) {
		this.id = id;
	}


	/**
	 * Gets {@link Service} with the given id.
	 *
	 * @param id - of the service
	 * @return an instance of the {@link Service}
	 */
	public static Service withId(String id) {
		return services.getOrDefault(id, create(id));
	}

	/**
	 * Creates a {@link Service} instance with the given id.
	 * The creation is thread-safe.
	 *
	 * @param id - of the service
	 * @return an instance of the {@link Service}
	 */
	private static Service create(String id) {
		Service service = services.computeIfAbsent(id, Service::new);

		ServiceRegistry sr = Service.getInfos(id, null, "code_provider_id");
		if (sr != null) {
			service.serviceProvider = ServiceProvider.withId(sr.getCodeProviderId());
		}

		return service;
	}

	/**
	 * Requests the list of {@link Service}s from DB.
	 *
	 * @return the list of {@link Service}s or an empty list on error
	 */
	public static List<Service> getAll() {
		List<ServiceRegistry> response;

		try {
			response = SERVICE_REGISTRY_API.serviceRegistryGet(null, null, null, null, null, null, null, "service_id,code_provider_id", null, null, null, null, null, null, null);
		} catch (RestClientException e) {
			log.error("Error fetching list of services.\n{}", e);
			return Collections.emptyList();
		}

		List<Service> serviceList = new ArrayList<>();
		Service service;

		for (ServiceRegistry sr : response) {
			service = Service.withId(sr.getServiceId());
			service.serviceProvider = ServiceProvider.withId(sr.getCodeProviderId());
			serviceList.add(service);
		}

		return serviceList;
	}


	/**
	 * Gets the id of the service.
	 *
	 * @return the id of the service
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the {@link ServiceProvider} of the service.
	 *
	 * @return the {@link ServiceProvider} of the service
	 */
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	/**
	 * Gets the price of the service.
	 *
	 * @return the price of the service
	 */
	public Double getPrice() {
		ServiceRegistry sr = getInfos("price");

		price = (sr == null ? null : sr.getPrice().doubleValue());

		return price;
	}

	/**
	 * Requests data specified in the select {@link String} from DB.
	 *
	 * @param select - specification of the data to request
	 * @return {@linkServiceRegistry} object with requested data or null on error
	 */
	public ServiceRegistry getInfos(String select) {
		return Service.getInfos(getId(), (getServiceProvider() == null ? null : getServiceProvider().getId()), select);
	}

	/**
	 * Requests data specified in the select {@link String} from DB.
	 *
	 * @param serviceId - service id of the requested service
	 * @param providerId - provider id of the requested service
	 * @param select - specification of the data to request
	 * @return {@linkServiceRegistry} object with requested data or null on error
	 */
	public static ServiceRegistry getInfos(String serviceId, String providerId, String select) {
		List<ServiceRegistry> serviceList;

		try {
			serviceList = SERVICE_REGISTRY_API.serviceRegistryGet((serviceId == null ? null : "eq." + serviceId), (providerId == null ? null : "eq." + providerId), null, null, null, null, null, select, null, null, null, null, null, null, null);
		} catch (RestClientException e) {
			log.error("Error fetching data from server for service with id '{}' and provider '{}'.\n{}", serviceId, providerId, e);
			return null;
		}

		if (serviceList.isEmpty()) {
			log.warn("No service found with id '{}' and provider '{}'.", serviceId, providerId);
			return null;
		}

		if (serviceList.size() > 1) {
			log.warn("More than one service found with id '{}' and provider '{}'. Choosing first one.", serviceId, providerId);
		}

		return serviceList.get(0);
	}
}
