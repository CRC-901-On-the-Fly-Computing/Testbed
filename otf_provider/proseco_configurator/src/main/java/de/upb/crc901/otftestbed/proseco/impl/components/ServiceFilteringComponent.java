package de.upb.crc901.otftestbed.proseco.impl.components;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.upb.crc901.otftestbed.postgrest.generated.java_client.api.ServiceRegistryApi;
import de.upb.crc901.otftestbed.postgrest.generated.java_client.model.ServiceRegistry;
import de.upb.crc901.otftestbed.proseco.impl.config.EnvironmentVariableConfiguration;

@Component
public class ServiceFilteringComponent {

	private static final Logger log = LoggerFactory.getLogger(ServiceFilteringComponent.class);

	@Autowired
	private ServiceRegistryApi serviceRegistry;

	@Autowired
	private RestTemplate restTemplate;

	/* The last time we checked for services in the gateway */
	private Instant lastPoll;

	/* The gateway-response */
	private Set<String> allServicesInGateway;

	private static final int POLL_PERIOD = 5;

	private String gatewayAddress;

	private String gatewayHeartbeat;

	@Autowired
	EnvironmentVariableConfiguration config;

	private static final String GATEWAY_GET_SERVICES_URI = "/cmd/executors/ls/services";

	private static final String GATEWAY_HEARTBEAT_URI = "/cmd/executors/heartbeat";

	@PostConstruct
	public void readGatewayFromEnv() {
		String gatewayHost = "execution-gateway." + config.getExecutionNamespace();
		String gatewayPort = "8080";
		gatewayAddress = new StringBuilder("http://").append(gatewayHost).append(":").append(gatewayPort)
				.append(GATEWAY_GET_SERVICES_URI).toString();
		gatewayHeartbeat = new StringBuilder("http://").append(gatewayHost).append(":").append(gatewayPort)
				.append(GATEWAY_HEARTBEAT_URI).toString();
	}

	public List<String> getAllServicesInDatabase() {
		List<ServiceRegistry> allValues = serviceRegistry.serviceRegistryGet(null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);
		log.debug("All services from db : {}", allValues);
		return allValues.stream().map(ServiceRegistry::getServiceId).collect(Collectors.toList());
	}

	/**
	 * Checks whether or not the service is registered in the gateway. Therefore, it
	 * polls the gateway for all available services (at most once per 5 seconds) and
	 * checks whether or not an executor provides this service.
	 * 
	 * @return true, if there is an executor that provides this service; false
	 *         otherwise
	 */
	public boolean serviceRegisteredInGateway(String serviceId) {
		// the current time to check if we need to refresh our cached services
		log.debug("service registration check in Gateway {}", serviceId);
		Instant now = Instant.now();
		if (allServicesInGateway == null || Duration.between(lastPoll, now).getSeconds() > POLL_PERIOD) {
			// either no cache yet; or cache invalid
			lastPoll = now;
			// poll the gateway
			allServicesInGateway = pollGatewayForServices();
		}
		log.debug("Registered: {}", allServicesInGateway.contains(serviceId));
		return allServicesInGateway.contains(serviceId);
	}

	private Set<String> pollGatewayForServices() {
		log.debug("Removing old executors...");
		restTemplate.getForEntity(gatewayHeartbeat, String.class);
		String rawOut = restTemplate.getForEntity(gatewayAddress, String.class).getBody();
		log.debug("Raw services in gateway are: {}", rawOut);
		Set<String> services = new HashSet<>();
		Scanner lineScanner = new Scanner(rawOut);
		while (lineScanner.hasNextLine()) {
			String line = lineScanner.nextLine();
			/*
			 * Executor ids start with a single tab, whereas the services start with two
			 * tabs
			 */
			if (line.startsWith("\t\t")) {
				String trimmed = line.trim();
				services.add(trimmed);
			}
		}
		lineScanner.close();
		log.debug("Got the services: {}", services);
		return services;
	}

	public List<String> getAllAvailableServices() {
		return getAllServicesInDatabase().stream().filter(this::serviceRegisteredInGateway)
				.collect(Collectors.toList());
	}
}
