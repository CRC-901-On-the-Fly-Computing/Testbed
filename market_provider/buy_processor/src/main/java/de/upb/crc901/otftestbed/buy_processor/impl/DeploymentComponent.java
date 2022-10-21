package de.upb.crc901.otftestbed.buy_processor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.buy_processor.impl.config.BuyProcessorConfig;
import de.upb.crc901.otftestbed.buy_processor.impl.exceptions.KubernertesCreationFailedException;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Component
public class DeploymentComponent {

	// tests the kubernetes connection and reads the master node ip
	public DeploymentComponent() {

		logger.debug("Reading service composition NS from env.");
		serviceCompositionNameSpace = System.getenv(SERVICE_COMPOSITION_NS_ENV_KEY);
		logger.debug("Reading docker secret from env.");
		dockerSecretName = System.getenv(DOCKER_SECRET_ENV_KEY);

		logger.debug("Testing kubernetes privileges");
		try (KubernetesClient client = new DefaultKubernetesClient()) {

			logger.debug("Checking GET access to NS {}", serviceCompositionNameSpace);

			client.extensions().deployments().inNamespace(serviceCompositionNameSpace).list();

			logger.debug("Checking GET access to Nodes ");

			List<Node> allNodes = client.nodes().list().getItems();

			logger.debug("Looking for the hostname of the master node");

			Optional<String> optionalHostName = Optional.ofNullable(System.getenv(MASTER_HOSTNAME_ENV_KEY));

			if (optionalHostName.isPresent()) {
				logger.debug("Reading master node hostname from env.");
				masterNodeHostname = optionalHostName.get();
			} else {

				Optional<Node> masterNode = allNodes.stream()
						.filter(node -> node.getSpec().getTaints().stream()
								.anyMatch(taint -> taint.getKey().equals("node-role.kubernetes.io/master")))
						.findFirst();

				if (masterNode.isPresent()) {
					logger.debug("Found tainted master node, reading hostname...");
					Optional<String> optHost = masterNode.get().getStatus().getAddresses().stream()
							.filter(addr -> addr.getType().equals("Hostname")).map(NodeAddress::getAddress)
							.findFirst();
					if (optHost.isPresent()) {
						masterNodeHostname = optHost.get();
					} else {
						logger.error("Could not read master nodes hostname!");
						masterNodeHostname = "unknown";
					}
				} else {
					logger.error("Could not read master nodes hostname!");
					masterNodeHostname = "unknown";
				}
			}
			logger.debug(
					"Created buy-processor with the following settings: serviceCompositionNS: {}, dockerSecretName: {}, masterHostname: {} ",
					serviceCompositionNameSpace, dockerSecretName, masterNodeHostname);
		} catch (io.fabric8.kubernetes.client.KubernetesClientException e) {
			throw new KubernertesCreationFailedException();
		}
	}

	@Autowired
	BuyProcessorConfig buyConfig;

	/*
	 * Specifies the environment variable that contains the hostname of the master
	 * node, please refer to the official helm chart documentation
	 */
	private static final String MASTER_HOSTNAME_ENV_KEY = "MASTER_NODE_HOSTNAME";

	/*
	 * Specifies the environment variable that contains the namespace to which the
	 * bought service compositions are deployed to, please refer to the official
	 * helm chart documentation
	 */
	private static final String SERVICE_COMPOSITION_NS_ENV_KEY = "SERVICE_COMPOSITION_NAMESPACE";

	/*
	 * Specifies the environment variable that contains the name of the kube secret
	 * that grants acces to the service compositions, please refer to the official
	 * helm chart documentation
	 */
	private static final String DOCKER_SECRET_ENV_KEY = "PRIVATE_DOCKER_SECRET_KEY";
	
	private static final String RABBIT_SECRET_NAME_ENV_KEY = "RABBIT_SECRET_NAME";
	
	private static final String MARKET_PROVIDER_NAMESPACE_ENV_KEY = "MARKET_PROVIDER_NAMESPACE";
	
	private final String rabbitSecretName = System.getenv(RABBIT_SECRET_NAME_ENV_KEY);

	private final String marketProvidernamespace = System.getenv(MARKET_PROVIDER_NAMESPACE_ENV_KEY);
	
	private final String dockerSecretName;

	private final String serviceCompositionNameSpace;

	private final String masterNodeHostname;

	private static final int CONTAINER_PORT = 8080;

	private static final String RUN_LABEL = "run";

	private static final String NAME_LABEL = "name";

	private static final String SERVICE_TYPE = "NodePort";

	Logger logger = LoggerFactory.getLogger(DeploymentComponent.class);

	public String deployDockerImage(String dockerTag, String hashOfOffer) {
		String deploymentIdentifier = dockerTag.substring(dockerTag.lastIndexOf('/') + 1);
		deploymentIdentifier = "z"
				+ deploymentIdentifier.replaceAll("-", "").substring(Math.max(deploymentIdentifier.length() - 7, 0));
		StringBuilder builder = new StringBuilder(deploymentIdentifier);
		builder.append("-").append(UUID.randomUUID().toString().substring(0, 4).toLowerCase());
		return deployDockerImage(builder.toString(), dockerTag, hashOfOffer);
	}

	/**
	 * 
	 * @param deploymentIdentifier
	 * @param dockerTag
	 * @return
	 */
	private String deployDockerImage(String deploymentIdentifier, String dockerTag, String hashOfOffer) {
		try (KubernetesClient client = new DefaultKubernetesClient()) {

			/** Create an exposed service for this deployment. */
			ServiceSpecs spec = deployService(deploymentIdentifier, client);

			/** Create a deployment for this user which will then be exposed. */
			Deployment deployment = createDeplyomentForName(deploymentIdentifier, dockerTag, spec.portEXEC,
					hashOfOffer);

			/** Register it as a deployment */
			client.extensions().deployments().inNamespace(serviceCompositionNameSpace).create(deployment);

			return spec.url;
		} catch (io.fabric8.kubernetes.client.KubernetesClientException e) {
			throw new KubernertesCreationFailedException();
		}
	}

	/**
	 * Creates a service on the kubernetes that exposes the deployment @param
	 * deploymentIdentifier.
	 * 
	 * @param deploymentIdentifier
	 *            the deployment to expose
	 * @param client
	 *            the kubernetes-client
	 * @return the exposed url of this service
	 */
	private ServiceSpecs deployService(String deploymentIdentifier, KubernetesClient client) {
		String serviceName = new StringBuilder(deploymentIdentifier).append("-service").toString();
		Map<String, String> selector = new HashMap<>();
		selector.put(NAME_LABEL, deploymentIdentifier);
		selector.put(RUN_LABEL, deploymentIdentifier);
		Service service = client.services().inNamespace(serviceCompositionNameSpace).createNew().withNewMetadata()
				.withName(serviceName.toLowerCase()).addToLabels(RUN_LABEL, deploymentIdentifier).endMetadata()
				.withNewSpec().addNewPort().withName("http").withPort(CONTAINER_PORT).withNewTargetPort()
				.withIntVal(CONTAINER_PORT).endTargetPort().endPort().addNewPort().withName("exec").withPort(7000)
				.withNewTargetPort().withIntVal(7000).endTargetPort().endPort().withType(SERVICE_TYPE)
				.withSelector(selector).endSpec().done();

		ServiceSpecs spec = new ServiceSpecs();
		for (ServicePort servicePort : service.getSpec().getPorts()) {
			if (servicePort.getName().equals("http")) {
				spec.portAPI = servicePort.getNodePort();
			}
			if (servicePort.getName().equals("exec")) {
				spec.portEXEC = servicePort.getNodePort();
			}
		}
		spec.url = new StringBuilder(masterNodeHostname).append(":").append(spec.portAPI).toString();

		return spec;
	}

	/**
	 * Creates a deployment on the kubernetes with the following specs:
	 * 
	 * * It runs the specified docker image (using the kubernetes secret) * It
	 * exposes the port 8000 of the docker container
	 * 
	 * @param name
	 *            the unique name of the deployment
	 * @param dockerImage
	 *            the image address of this deployment, this image will be pulled
	 *            using the <code>KUBE_SECRET</code>
	 * @param serviceURL
	 *            the exposed address of this deployment, thus the service has to be
	 *            created before the deployment is
	 * 
	 * @return the deployment sepc
	 */
	private Deployment createDeplyomentForName(String deploymentIdentifier, String dockerImage, int portEXEC,
			String hashOfOffer) {
		String ipAddress = masterNodeHostname.concat(":").concat(Integer.toString(portEXEC));
		return new DeploymentBuilder().withNewMetadata().withName(deploymentIdentifier).endMetadata().withNewSpec()
				.withReplicas(1).withRevisionHistoryLimit(0)
				.withNewTemplate().withNewMetadata()
				.addToLabels(RUN_LABEL, deploymentIdentifier).addToLabels(NAME_LABEL, deploymentIdentifier).endMetadata()
				.withNewSpec().addNewContainer().withName(deploymentIdentifier).withImage(dockerImage)
				.addNewPort().withContainerPort(CONTAINER_PORT).endPort()
				.addNewPort().withContainerPort(7000).endPort()
				.addNewEnv().withName("EXECUTOR_ADDRESS").withValue(ipAddress).endEnv()
				.addNewEnv().withName("QUEUE").withValue(hashOfOffer).endEnv()
				.addNewEnv().withName("RABBIT_MQ_HOST").withValue(marketProvidernamespace + "-" + "composition-queue."+marketProvidernamespace).endEnv()
				.addNewEnv().withName("RABBIT_MQ_PORT").withValue("5672").endEnv()
				.addNewEnv().withName("RABBIT_MQ_USER").withNewValueFrom().withNewSecretKeyRef("username", rabbitSecretName, false).endValueFrom().endEnv()
				.addNewEnv().withName("RABBIT_MQ_PASSWORD").withNewValueFrom().withNewSecretKeyRef("password", rabbitSecretName, false).endValueFrom().endEnv()

				.endContainer()
				
				.withImagePullSecrets(new LocalObjectReference(dockerSecretName)).endSpec().endTemplate().endSpec()
				.build();
	}

	class ServiceSpecs {
		String url;
		int portAPI;
		int portEXEC;
	}

}
