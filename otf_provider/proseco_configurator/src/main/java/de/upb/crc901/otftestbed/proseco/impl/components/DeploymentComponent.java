package de.upb.crc901.otftestbed.proseco.impl.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.proseco.impl.config.EnvironmentVariableConfiguration;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarSource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.SecretKeySelector;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Handles the deployment of a proseco-pod to kubernetes. The proseco-pod is
 * based on a template that resides in the <code>conf/proseco_pod.yaml</code>.
 * For this class to work, this class has to be able to communicate with the
 * kubernetes-api. Therefore, we deploy the proseco-configurator with a
 * specified ServiceAccount that is specified in the helm configuration.
 * 
 * @author Mirko Jürgens
 *
 */
@Component
public class DeploymentComponent {

	@Autowired
	private EnvironmentVariableConfiguration config;

	private static final Logger log = LoggerFactory.getLogger(DeploymentComponent.class);

	/**
	 * Spawns a proseco-pod using the default kubernetes configuration. The
	 * {@link KubernetesClient} is auto configured, hence it will use the
	 * ServiceAccount for communicating with the API. If this component is run in an
	 * offline setting this client uses the kube config at
	 * <code>${HOME}/.kube/config</code>
	 * 
	 * The pod gets configured with various context specific information such as the
	 * market & otf provider namespaces.
	 * 
	 * @param requestUUID the unique request uuid.
	 */
	public void spawnProsecoPod(UUID requestUUID) {

		try (KubernetesClient client = new DefaultKubernetesClient()) {
			List<HasMetadata> yamlComponents = client.load(new FileInputStream("conf/proseco_pod.yaml")).get();
			if (yamlComponents.isEmpty()) {
				log.error("Couldn't parse pod template");
				throw new IllegalStateException("Couldn't parse pod template");
			}

			Pod pod = (Pod) yamlComponents.get(0);

			/** Set the name of the pod to the request uuid s.t. we can later find it */
			pod.getMetadata().setName(requestUUID.toString());
			/** Set the correct namespace according to the helm-config */
			pod.getMetadata().setNamespace(config.getPodNamespace());

			/** Set the image credential s.t. kubernetes can pull the image */
			pod.getSpec().setImagePullSecrets(Arrays.asList(new LocalObjectReference(config.getDockerSecretName())));

			Container prosecoContainer = pod.getSpec().getContainers().get(0);

			/**
			 * Mount the docker credentials as kube secret s.t. the pod can push the
			 * configured container when it has finished configuring
			 */
			prosecoContainer.getEnv().add(new EnvVar("DOCKER_USER", null, new EnvVarSource(null, null, null,
					new SecretKeySelector("username", config.getDockerCredentialsecretName(), false))));
			prosecoContainer.getEnv().add(new EnvVar("DOCKER_PASSWORD", null, new EnvVarSource(null, null, null,
					new SecretKeySelector("password", config.getDockerCredentialsecretName(), false))));
			prosecoContainer.getEnv().add(new EnvVar("DOCKER_HOST", null, new EnvVarSource(null, null, null,
					new SecretKeySelector("host", config.getDockerCredentialsecretName(), false))));

			/**
			 * Mount the request database secret into the pod s.t. the pod can retrieve all
			 * interview information
			 */

			prosecoContainer.getEnv().add(new EnvVar("REQUEST_DB_USER", null, new EnvVarSource(null, null, null,
					new SecretKeySelector("username", config.getRequestDBSecretName(), false))));
			prosecoContainer.getEnv().add(new EnvVar("REQUEST_DB_PASSWORD", null, new EnvVarSource(null, null, null,
					new SecretKeySelector("password", config.getRequestDBSecretName(), false))));

			/**
			 * Mount the request-database host & port
			 */
			prosecoContainer.getEnv().add(new EnvVar(EnvironmentVariableConfiguration.REQUEST_DB_PORT_ENV_KEY,
					config.getRequestDBPort(), null));
			prosecoContainer.getEnv().add(
					new EnvVar(EnvironmentVariableConfiguration.REQUEST_DB_HOST_ENV_KEY, config.getDbHost(), null));

			/**
			 * Mount Request-UUID & OTFP-UUID into the pod, the former for processing the
			 * request and the latter for entering the offer as the correct otf-provider
			 */
			prosecoContainer.getEnv().add(new EnvVar("REQUEST_UUID", requestUUID.toString(), null));
			prosecoContainer.getEnv().add(new EnvVar("OTFP_UUID",
					config.getOtfpUUID().toString(), null));

			/**
			 * Set the PROXY_ADDRESS for the execution part
			 */
			prosecoContainer.getEnv()
					.add(new EnvVar("PROXY_ADDRESS", "proxy." + config.getExecutionNamespace() + ":8080", null));

			/** Set the otf & market provider namespaces */
			prosecoContainer.getEnv().add(new EnvVar(EnvironmentVariableConfiguration.OTFP_NAMESPACE_ENV_KEY,
					config.getOtfpNamespace(), null));
			prosecoContainer.getEnv().add(new EnvVar(EnvironmentVariableConfiguration.MARKET_PROVIDER_NAMESPACE_ENV_KEY,
					config.getMarketProviderNamespace(), null));

			/** Finally, create the pod */
			client.pods().inNamespace(config.getPodNamespace()).create(pod);

		} catch (FileNotFoundException e) {
			log.error("Failed to find pod template");
		}
	}
}
