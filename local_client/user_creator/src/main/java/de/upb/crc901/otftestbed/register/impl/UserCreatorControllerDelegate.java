package de.upb.crc901.otftestbed.register.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.register.generated.spring_server.api.UserCreatorApiDelegate;
import de.upb.crc901.otftestbed.register.impl.exceptions.KubernertesCreationFailedException;
import de.upb.crc901.otftestbed.register.impl.exceptions.RegistrationFailedException;
import de.upb.crc901.otftestbed.register.impl.exceptions.ServiceRequesterCountException;
import de.upb.crc901.otftestbed.register.impl.exceptions.UnsupportedUsernameException;
import de.upb.crc901.otftestbed.register.impl.exceptions.UsernameNotUniqueException;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Class that can deploy services in kubernetes, for taht the kube-config is
 * mounted into this service such that in can communicate with the kubernetes
 * api.
 * 
 * @author mirkoj
 *
 */
@Component
public class UserCreatorControllerDelegate implements UserCreatorApiDelegate {

	/** The logger :D */
	private static final Logger log = LoggerFactory.getLogger(UserCreatorControllerDelegate.class);

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * The docker tag of the image to deploy
	 */
	private static final String DOCKER_IMAGE = "nexus.cs.upb.de/sfb901-testbed/service_requester_new:v42";

	/**
	 * Some naming
	 */
	private static final String COMPONENT_NAME = "service-requester-new";
	/**
	 * The internal port of the docker image
	 */
	private static final int DOCKER_PORT = 8080;

	/**
	 * The Kube-Secret that contains the authentication information for pulling from
	 * the docker registry.
	 */
	private static final String KUBE_SECRET_NAME = "myregistrykey";

	/**
	 * The namespace where the deployment should be created.
	 */
	private static final String KUBE_NAMESPACE = "users";

	private static final String SERVICE_TYPE = "NodePort";

	private static final String KUBE_HTTP_IP = "https://131.234.62.29";


	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}


	/**
	 * Tries to register a new service_requester instance in the kubernetes cluster.
	 */
	@Override
	public ResponseEntity<SimpleJSONMessage> registerUser(SimpleAttributes body, String name) {

		/**
		 * Check whether the given username can be used to reference the kubernetes
		 * service.
		 */
		String regex = "[a-z0-9]*";

		if (!Pattern.matches(regex, name)) {
			throw new UnsupportedUsernameException();
		}

		/** Set up a kubernetes client with the given kubernetes secrets. */
		Service service = null;
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			log.debug("Creating the deplyoment...");

			/** Create a deployment for this user which will then be exposed. */
			Deployment deployment = createDeploymentForName(name);

			/** Register it as a deployment */
			client.extensions().deployments().inNamespace(KUBE_NAMESPACE).create(deployment);

			log.debug("Exposing the deployment...");

			/** Create an exposed service for this deployment. */
			String serviceName = new StringBuilder(name).append("service").toString();
			Map<String, String> selector = new HashMap<>();
			selector.put("name", name);
			selector.put("run", COMPONENT_NAME);
			service = client.services().inNamespace(KUBE_NAMESPACE).createNew().withNewMetadata().withName(serviceName)
					.addToLabels("run", COMPONENT_NAME).endMetadata().withNewSpec().addNewPort().withPort(80)
					.withNewTargetPort().withIntVal(DOCKER_PORT).endTargetPort().endPort().withType(SERVICE_TYPE)
					.withSelector(selector).endSpec().done();

		} catch (io.fabric8.kubernetes.client.KubernetesClientException e) {

			log.error("Something went wrong while trying to set-up the kubernetes deployments...", e);
			if (e.getMessage().contains("code=409")) {
				// this is the code for a duplicate deployment-entry
				throw new UsernameNotUniqueException();
			}
			// something else went wrong (most likely no connection to the kubernetes could
			// be established)
			throw new KubernertesCreationFailedException();
		}
		log.debug("Waiting for the service to boot...");
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		log.debug("Retrieving the exposed port...");
		int exposedPort = service.getSpec().getPorts().iterator().next().getNodePort();
		String userApi = new StringBuilder(KUBE_HTTP_IP).append(":").append(exposedPort).append("/api").toString();
		log.debug("The service is running at {}", userApi);

//		telemetry.logRequesterAdded(String.valueOf(exposedPort));

		log.debug("Trying to register the user in the reputation system...");
		String registerApi = userApi + "/register";
		try {
			ResponseEntity<SimpleJSONMessage> response = restTemplate.postForEntity(registerApi, body,
					SimpleJSONMessage.class);
			log.debug("Rest-Call returned: {}", response.getBody().getMessage());
		} catch (org.springframework.web.client.ResourceAccessException e1) {
			log.error("I/O Exception while trying to invoke the registration", e1);
			return new ResponseEntity<>(new SimpleJSONMessage(
					"Failed to register, try it manually at http://sfb-k8master-1.cs.uni-paderborn.de:" + exposedPort + "/api"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			log.error("Registration failed nested exception ", e);
			throw new RegistrationFailedException();
		}
		log.debug("Successfully set-up the user. Returning api-link");
		return new ResponseEntity<>(new SimpleJSONMessage("http://sfb-k8master-1.cs.uni-paderborn.de:" + exposedPort + "/api"),
				HttpStatus.OK);
	}

	private Deployment createDeploymentForName(String name) {
		return new DeploymentBuilder().withNewMetadata().withName(name).endMetadata().withNewSpec().withReplicas(1)
				.withNewTemplate().withNewMetadata().addToLabels("run", COMPONENT_NAME).addToLabels("name", name)
				.endMetadata().withNewSpec().addNewContainer().withName(COMPONENT_NAME).withImage(DOCKER_IMAGE)
				.addNewPort().withContainerPort(DOCKER_PORT).endPort().endContainer()
				.withImagePullSecrets(new LocalObjectReference(KUBE_SECRET_NAME)).endSpec().endTemplate().endSpec()
				.build();
	}

	/**
	 * Get the number of active Service-Requesters. Actually it counts the number
	 * of services on the kubernetes in the namespace users.
	 * @return number of active Service-Requesters.
	 * @throws ServiceRequesterCountException
	 */
	private int numberServiceRequesters() throws ServiceRequesterCountException {
		log.debug("Getting number of Service-Requesters.");
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			return client.services().inNamespace(KUBE_NAMESPACE).list().getItems().size();
		} catch (Exception e) {
			log.error("Error in getting number of Service-Requesters.");
			throw new ServiceRequesterCountException();
		}
	}

	/**
	 * Get the number of active Service-Requesters.
	 */
	@Override
	public ResponseEntity<Integer> getNumberServiceRequesters() {
		return new ResponseEntity<>(numberServiceRequesters(), HttpStatus.OK);
	}
}
