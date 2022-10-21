package de.upb.crc901.otftestbed.kubernetes_spawner.impl.model;

import de.upb.crc901.otftestbed.commons.admin.ExecutorDescription;
import de.upb.crc901.otftestbed.commons.admin.ExecutorLog;
import de.upb.crc901.otftestbed.commons.admin.ExecutorLogs;
import de.upb.crc901.otftestbed.commons.admin.ListOfExecutors;
import de.upb.crc901.otftestbed.commons.spawner.ListOfServices;
import de.upb.crc901.otftestbed.kubernetes_spawner.impl.config.Config;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KubernetesComponent {

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
    private static final String EXECUTOR_NS_ENV_KEY = "EXECUTOR_NAMESPACE";

    /*
     * Specifies the environment variable that contains the name of the kube secret
     * that grants access to the service compositions, please refer to the official
     * helm chart documentation
     */
    private static final String DOCKER_SECRET_ENV_KEY = "PRIVATE_DOCKER_SECRET_KEY";

    /*
     * Specifies the environment variable where the callback URL of this executor
     * should be written to
     */
    private static final String EXECUTOR_ADDRESS_ENV_KEY = "EXECUTOR_ADDRESS";

    /*
     * Specifies the environment variable that contains the host of the executor
     */
	private static final String EXECUTOR_ADDRESS_HOST_ENV_KEY = "EXECUTOR_ADDRESS_HOST";

	/*
	 * Specifies the environment variable that contains the id of the executor
	 */
	private static final String EXECUTOR_ID_ENV_KEY = "SEDE_EXECUTOR_ID";

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(KubernetesComponent.class);

    private final String dockerSecretName;

    private final String executorNamespace;

    private final String masterNodeHostname;

    private static final int CONTAINER_PORT = 8000;

    private static final String NAME_LABEL = "name";

	private static final String RUN_LABEL = "run";

	private static final String SEDE_VOLUME_NAME = "sede-volume";

	private static final String BACKUP_VOLUME_NAME = "backup-volume";

	private static final String INSTANCES_VOLUME_NAME = "instances-volume";

	private static final List<String> NODE_ANTI_AFFINITY = Collections.unmodifiableList(Collections.singletonList("sfb-jenkins"));


    public KubernetesComponent() {

        logger.debug("Reading service composition NS from env.");
        executorNamespace = System.getenv(EXECUTOR_NS_ENV_KEY);
        logger.debug("Reading docker secret from env.");
        dockerSecretName = System.getenv(DOCKER_SECRET_ENV_KEY);

        logger.debug("Testing kubernetes privileges");
        try (KubernetesClient client = new DefaultKubernetesClient()) {

            logger.debug("Checking GET access to NS {}", executorNamespace);

            client.apps().deployments().inNamespace(executorNamespace).list();

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
                            .filter(addr -> addr.getType().equals("Hostname")).map(NodeAddress::getAddress).findFirst();
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
                    "Created executor-spawner with the following settings: serviceCompositionNS: {}, dockerSecretName: {}, masterHostname: {} ",
                    executorNamespace, dockerSecretName, masterNodeHostname);
        } catch (io.fabric8.kubernetes.client.KubernetesClientException e) {
        	logger.error("Error initializing KubernetesComponent", e);
            throw new KubernetesCreationFailedException();
        }
    }

    /**
     * @param serviceId
     * @param dockerTag
     * @return
     */
    public String deployDockerImage(String serviceId, String dockerTag) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {

            /** Create an exposed service for this deployment. */
            String serviceURL = deployService(serviceId, client);

            /** Create a PVC for this deployment. */
            PersistentVolumeClaim claim = createPersistentVolumeClaim("backup", serviceId, client);

            /** Register it as a PVC */
            client.persistentVolumeClaims().inNamespace(executorNamespace).create(claim);

			/** Create a PVC for this deployment. */
            claim = createPersistentVolumeClaim("instances", serviceId, client);

			/** Register it as a PVC */
            client.persistentVolumeClaims().inNamespace(executorNamespace).create(claim);

            /** Create a deployment for this user which will then be exposed. */
            Deployment deployment = createDeploymentForName(serviceId, dockerTag);

            /** Register it as a deployment */
            client.apps().deployments().inNamespace(executorNamespace).create(deployment);

            /** Create a HPA for this deployment. */
            HorizontalPodAutoscaler hpa = createHorizontalPodAutoscaler(serviceId, client);

            /** Register it as a HPA */
            client.autoscaling().horizontalPodAutoscalers().inNamespace(executorNamespace).create(hpa);

            return serviceId;
        } catch (KubernetesClientException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Loads first kubernetes resource from given resource file.
	 *
	 * @param resource  file to load kubernetes resource from
	 * @param client    kubernetes client used to load resource
	 * @param <T>       type of loaded kubernetes resource
	 * @return the loaded kubernetes resource
	 * @throws ClassCastException if the loaded resource type differs from parameter type {@code T}
	 */
    private <T extends HasMetadata> T loadYaml(String resource, KubernetesClient client) {
		List<HasMetadata> yamlComponents = client.load(getClass().getClassLoader().getResourceAsStream(resource)).get();

		if (yamlComponents.isEmpty()) {
			logger.error("Couldn't load kubernetes template {}", resource);
			throw new IllegalStateException("Couldn't load kubernetes template");
		}

		try {
			@SuppressWarnings("unchecked")
			T ret = (T) yamlComponents.get(0);
			return ret;
		} catch(ClassCastException e) {
			throw e;
		}
	}

    /**
     * Creates a service on the kubernetes that exposes the deployment @param
     * id.
     *
     * @param id the deployment to expose
     * @param client
     * @return the exposed url of this service
     */
    private String deployService(String id, KubernetesClient client) {
    	String lowId = id.toLowerCase();

    	Map<String, String> labels = new HashMap<>();
		labels.put(NAME_LABEL, lowId);
		labels.put(RUN_LABEL, lowId);

		Service service = loadYaml("k8s/executor_service.yaml", client);
		service = new ServiceBuilder(service)
				.editOrNewMetadata()
					.withName(lowId)
					.withNamespace(executorNamespace)
					.addToLabels(labels)
				.endMetadata()
				.editOrNewSpec()
					.addToSelector(labels)
					.editMatchingPort(p -> "tcp".equals(p.getName()))
						.withNewTargetPort(CONTAINER_PORT)
					.endPort()
				.endSpec()
				.build();

		service = client.services().inNamespace(executorNamespace).create(service);
		int exposedPort = service.getSpec().getPorts().get(0).getNodePort();

    	return masterNodeHostname + ":" + exposedPort;
    }

    /**
     * Creates a deployment on the kubernetes with the following specs:
     * <p>
     * * It runs the specified docker image (using the kubernetes secret) * It
     * exposes the port 8000 of the docker container * It exposes the port 2200 of
     * the docker container * It mounts the exposed address of this deployment into
     * the deployment using the environment variable 'EXECUTOR_ADDRESS'
     *
     * @param id the unique name of the deployment
     * @param dockerImage          the image address of this deployment, this image will be
     *                             pulled using the <code>KUBE_SECRET</code>
     * @return the deployment spec
     */
    private Deployment createDeploymentForName(String id, String dockerImage) {
    	String lowId = id.toLowerCase();

		Map<String, String> labels = new HashMap<>();
		labels.put(NAME_LABEL, lowId);
		labels.put(RUN_LABEL, lowId);

		StringBuilder sbURL = new StringBuilder();
		sbURL.append("$(");
		sbURL.append(EXECUTOR_ADDRESS_HOST_ENV_KEY);
		sbURL.append("):");
		sbURL.append(CONTAINER_PORT);

		return new DeploymentBuilder()
				.withNewMetadata()
					.withName(lowId)
					.withNamespace(executorNamespace)
					.addToLabels(labels)
				.endMetadata()
				.withNewSpec()
					.withRevisionHistoryLimit(0)
					.withNewSelector()
						.addToMatchLabels(labels)
					.endSelector()
					.withNewTemplate()
						.withNewMetadata()
							.addToLabels(labels)
						.endMetadata()
						.withNewSpec()
							.addNewInitContainer()
								.withName("generate-copy")
								.withImage(dockerImage)

								.addNewCommand("/temp/init.sh")

								.addNewVolumeMount()
									.withName(SEDE_VOLUME_NAME)
									.withMountPath(Config.SEDE_PATH)
								.endVolumeMount()
								.addNewVolumeMount()
									.withName(BACKUP_VOLUME_NAME)
									.withMountPath(Config.SEDE_BACKUP_PATH)
								.endVolumeMount()
							.endInitContainer()

							.addNewContainer()
								.withName(lowId)
								.withImage(dockerImage)

								.addNewPort()
									.withContainerPort(CONTAINER_PORT)
								.endPort()

								.withNewResources()
									.addToRequests("cpu", new Quantity("0.5"))
									.addToLimits("cpu", new Quantity("1"))
								.endResources()

								.addNewEnv()
									.withName(EXECUTOR_ADDRESS_HOST_ENV_KEY)
									.withNewValueFrom()
										.withFieldRef(new ObjectFieldSelector(null, "status.podIP"))
									.endValueFrom()
								.endEnv()
								.addNewEnv()
									.withName(EXECUTOR_ADDRESS_ENV_KEY)
									.withValue(sbURL.toString())
								.endEnv()
								.addNewEnv()
									.withName(EXECUTOR_ID_ENV_KEY)
									.withNewValueFrom()
										.withFieldRef(new ObjectFieldSelector(null, "metadata.name"))
									.endValueFrom()
								.endEnv()

								.addNewVolumeMount()
									.withName(SEDE_VOLUME_NAME)
									.withMountPath(Config.SEDE_PATH)
								.endVolumeMount()
								.addNewVolumeMount()
									.withName(INSTANCES_VOLUME_NAME)
									.withMountPath(Config.SEDE_INSTANCES_PATH)
								.endVolumeMount()
							.endContainer()
							.withImagePullSecrets(new LocalObjectReference(dockerSecretName))

							.addNewVolume()
								.withName(SEDE_VOLUME_NAME)
								.withNewEmptyDir()
								.endEmptyDir()
							.endVolume()
							.addNewVolume()
								.withName(BACKUP_VOLUME_NAME)
								.withNewPersistentVolumeClaim()
									.withClaimName(lowId + "-backup")
								.endPersistentVolumeClaim()
							.endVolume()
							.addNewVolume()
								.withName(INSTANCES_VOLUME_NAME)
								.withNewPersistentVolumeClaim()
									.withClaimName(lowId + "-instances")
								.endPersistentVolumeClaim()
							.endVolume()

							.withNewAffinity()
								.withNewNodeAffinity()
									.withNewRequiredDuringSchedulingIgnoredDuringExecution()
										.addNewNodeSelectorTerm()
											.addNewMatchExpression()
												.withKey("kubernetes.io/hostname")
												.withOperator("NotIn")
												.addAllToValues(NODE_ANTI_AFFINITY)
											.endMatchExpression()
										.endNodeSelectorTerm()
									.endRequiredDuringSchedulingIgnoredDuringExecution()
								.endNodeAffinity()
							.endAffinity()
						.endSpec()
					.endTemplate()
				.endSpec()
				.build();
    }

    private PersistentVolumeClaim createPersistentVolumeClaim(String type, String id, KubernetesClient client) {
		String lowId = id.toLowerCase();
		String name = (type == null ? lowId : lowId + "-" + type.toLowerCase());

		Map<String, String> labels = new HashMap<>();
		labels.put(NAME_LABEL, lowId);
		labels.put(RUN_LABEL, lowId);

		PersistentVolumeClaim claim = loadYaml("k8s/executor_pvc.yaml", client);
		claim = new PersistentVolumeClaimBuilder(claim)
				.editOrNewMetadata()
					.withName(name)
					.withNamespace(executorNamespace)
					.addToLabels(labels)
				.endMetadata()
				.build();

		return claim;
    }

    private HorizontalPodAutoscaler createHorizontalPodAutoscaler(String id, KubernetesClient client) {
		String lowId = id.toLowerCase();

		Map<String, String> labels = new HashMap<>();
		labels.put(NAME_LABEL, lowId);
		labels.put(RUN_LABEL, lowId);

		HorizontalPodAutoscaler autoscaler = loadYaml("k8s/executor_hpa.yaml", client);
		autoscaler = new HorizontalPodAutoscalerBuilder(autoscaler)
				.editOrNewMetadata()
					.withName(lowId)
					.withNamespace(executorNamespace)
					.addToLabels(labels)
				.endMetadata()
				.editOrNewSpec()
					.editOrNewScaleTargetRef()
						.withName(lowId)
					.endScaleTargetRef()
				.endSpec()
				.build();

		return autoscaler;
    }

    /**
     * Returns if a Executor with the ID is existing
     *
     * @param executorId Id of the executor to search for
     * @return whether the executor is available
     */
    public boolean isExisting(UUID executorId) {
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            logger.debug("Check if Executor exists");
            boolean executorExists = kubernetesClient.apps().deployments().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()) != null;
            logger.debug("Answer will be " + executorExists);
            return executorExists;
        } catch (KubernetesClientException kubeException) {
            logger.error("An error occurred while validate the executor", kubeException);
        }
        return false;
    }

    /**
     * Delete the Executor from the kubernetes
     *
     * @param executorId Id of the executor to delete
     * @return whether the job was successfully completed
     */
    public boolean destroyExecutor(UUID executorId) throws KubernetesClientException {
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            boolean success = true;

            logger.debug("Try to delete Deployment, PVC, HPA and Service");
            success = deleteService(executorId, kubernetesClient) && success;
            success = deleteDeployment(executorId, kubernetesClient) && success;
            success = deletePVC(executorId, kubernetesClient) && success;
            success = deleteHorizontalPodAutoscaler(executorId, kubernetesClient) && success;

            return success;
        } catch (KubernetesClientException kubeException) {
            logger.error("An error occurred while deleting the executor", kubeException);
        }
        return false;
    }

    private boolean deleteDeployment(UUID executorId, KubernetesClient kubernetesClient) {
        boolean success = kubernetesClient.apps().deployments().inNamespace(executorNamespace).withName(executorId.toString()).cascading(true).delete();
        logger.debug("Delete Deployment is {}", success);
        return success;
    }

    private boolean deletePVC(UUID executorId, KubernetesClient kubernetesClient) {
        boolean success = kubernetesClient.persistentVolumeClaims().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()).delete();
        logger.debug("Delete PVC is {}", success);
        return success;
    }

    private boolean deleteHorizontalPodAutoscaler(UUID executorId, KubernetesClient kubernetesClient) {
        boolean success = kubernetesClient.autoscaling().horizontalPodAutoscalers().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()).delete();
        logger.debug("Delete HPA is {}", success);
        return success;
    }

    private boolean deleteService(UUID executorId, KubernetesClient kubernetesClient) {
        boolean success = kubernetesClient.services().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()).delete();
        logger.debug("Delete Service is {}", success);
        return success;
    }

    public ExecutorDescription getExecutor(UUID executorId) {
        ExecutorDescription executorDescription = new ExecutorDescription();
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            logger.debug("");
            if (kubernetesClient.apps().deployments().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()) != null)
                executorDescription.setExecutorName(executorId);
            ServiceList list = kubernetesClient.services().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()).list();
            ListOfServices listOfServices = new ListOfServices();
            for (Service service :
                    list.getItems()) {
                String serviceName = service.getMetadata().getLabels().get(NAME_LABEL);
                listOfServices.getServices().add(serviceName);
            }
            executorDescription.setListOfServices(listOfServices);
        } catch (KubernetesClientException kubeException) {
            logger.error("An error occurred while getting the executor", kubeException);
        }
        return executorDescription;
    }

    public ResponseEntity<ListOfExecutors> getExecutors() {
        ListOfExecutors listOfExecutors = new ListOfExecutors();
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            logger.debug("Try to get all executors");
            DeploymentList deploymentList = kubernetesClient.apps().deployments().inNamespace(executorNamespace).list();
            for (Deployment deployment :
                    deploymentList.getItems()) {
                ExecutorDescription executorDescription = new ExecutorDescription();
                executorDescription.setExecutorName(UUID.fromString(deployment.getMetadata().getName()));
                ServiceList serviceList = kubernetesClient.services().inNamespace(executorNamespace).withLabel(NAME_LABEL, deployment.getMetadata().getName()).list();
                ListOfServices listOfServices = new ListOfServices();
                for (int i = 0; i < serviceList.getItems().size(); i++) {
                    String name = serviceList.getItems().get(i).getMetadata().getName();
                    listOfServices.getServices().add(name);
                }
                executorDescription.setListOfServices(listOfServices);
                listOfExecutors.add(executorDescription);
            }
            return new ResponseEntity<>(listOfExecutors, HttpStatus.OK);
        } catch (KubernetesClientException kubeException) {
            logger.error("An error occurred while getting the executors", kubeException);
        }
        return null;
    }

    public ResponseEntity<ExecutorLogs> getLogs(UUID executorId) {
        ExecutorLogs executorLogs = new ExecutorLogs();
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            kubernetesClient.pods().inNamespace(executorNamespace).withLabel(NAME_LABEL, executorId.toString()).watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod resource) {
                    ExecutorLog executorLog = new ExecutorLog();
                    executorLog.setPlaceholder(resource.getMetadata().getName() + action);
                    executorLogs.add(executorLog);
                }

                @Override
                public void onClose(KubernetesClientException cause) {
                    ExecutorLog executorLog = new ExecutorLog();
                    executorLog.setPlaceholder(cause.getMessage());
                }
            });
            return new ResponseEntity<>(executorLogs, HttpStatus.OK);
        } catch (KubernetesClientException kubeException) {
            logger.error("An error occurred while getting the executors", kubeException);
        }
        return null;
    }

    // ergibt es überhaupt sinn die einzelnen Logs zu löschen ?
//    public boolean deleteLogs(UUID executorId) {
//        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()){
//
//        } catch (KubernetesClientException kubeException) {
//            logger.error("An error occurred while deleting the executors logs", kubeException);
//        }
//        return true;
//    }
}
