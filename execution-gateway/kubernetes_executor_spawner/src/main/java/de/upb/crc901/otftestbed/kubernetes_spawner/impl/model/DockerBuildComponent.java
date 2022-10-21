package de.upb.crc901.otftestbed.kubernetes_spawner.impl.model;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ProgressMessage;
import com.spotify.docker.client.messages.RegistryAuth;

/**
 * A component that can asynchronously build containers and upload them to a
 * separate nexus repository.
 * 
 * @author elppa
 *
 */
@Component
public class DockerBuildComponent {

	private final DockerClient dockerClient;

	private final RegistryAuth auth;

	private static final String LINUX_DOCKER_HOST = "unix:///var/run/docker.sock";

	private static final String NEXUS_U = System.getenv("NEXUS_USER");

	private static final String NEXUS_P = System.getenv("NEXUS_PASSWORD");

	private static final String TAG_PREFIX = System.getenv("NEXUS_HOST");

	private static final String NEXUS_HOST = "https://" + TAG_PREFIX;

	private static final Logger logger = LoggerFactory.getLogger(DockerBuildComponent.class);

	public DockerBuildComponent() {
		if (SystemUtils.IS_OS_WINDOWS) {
			logger.info("Windows OS detected, trying to start docker-client...");
			try {
				dockerClient = DefaultDockerClient.fromEnv().build();
			} catch (DockerCertificateException e) {
				logger.error("Failed to load docker ...", e);
				throw new DockerClientStartupFailedException(e);
			}

		} else if (SystemUtils.IS_OS_MAC_OSX) {
			logger.info("Mac OSX detected, trying to start docker-client...");

			dockerClient = new DefaultDockerClient(LINUX_DOCKER_HOST);
			System.setProperty("DOCKER_HOST", LINUX_DOCKER_HOST);

		} else if (SystemUtils.IS_OS_LINUX) {
			logger.info("Linux OS detected, trying to start docker-client...");
			dockerClient = new DefaultDockerClient(LINUX_DOCKER_HOST);
			System.setProperty("DOCKER_HOST", LINUX_DOCKER_HOST);

		} else {
			logger.error("Found unsupported OS! Exiting");
			throw new DockerClientStartupFailedException();
		}
		auth = RegistryAuth.builder().serverAddress(NEXUS_HOST).username(NEXUS_U).password(NEXUS_P).build();
		try {
			logger.info("Authenticating... return code is {}", dockerClient.auth(auth));
			String dockerReturn = dockerClient.ping();
			if (!"OK".equals(dockerReturn))
				logger.error("Pinging the nexus returned ...{}", dockerReturn);
			logger.info("Ping to docker returned {}", dockerReturn);
		} catch (DockerException | InterruptedException e) {
			logger.error("Docker auth failed!", e);
			Thread.currentThread().interrupt();
			throw new DockerClientStartupFailedException();
		}
	}

	/**
	 * Requests the docker-client to build the project specified by this name, and
	 * upload it to the nexus registry.
	 * 
	 * @param folderName the folderName of this project. E.g. if the resulting
	 *                   project is in the (relative) folder
	 *                   <code>kubernetes_executor_spawner/docker_images/automl-05A8DEB2D0</code>
	 *                   the folder name would be <code> automl-05A8DEB2D0</code>.
	 * @param tagName    the (unique!) tag-name of the resulting project. This will
	 *                   be used to tag the docker image in the nexus project. E.g.
	 *                   if you provide the tag <code>automl-05A8DEB2D0</code> then
	 *                   the resulting docker tag is
	 *                   <code>kube-node-gamma.cs.upb.de:8081/repository/service-compositions/automl-05A8DEB2D0</code>.
	 *                   Additionally, this tag is mounted into the docker-container
	 *                   to verify the access. In other words, when a user has an
	 *                   authentication token that verifies that he bought this
	 *                   tagName, this service will accept that token.
	 * 
	 */
	public String buildAndUploadDockerImage(final String folderName, final String tagName, final String executorName) {
		final String escapedTagName = tagName.toLowerCase();

		logger.debug("Starting to build {}", executorName);
		try {

			dockerClient.build(FileSystems.getDefault().getPath("docker_images", folderName).toAbsolutePath(),
					escapedTagName, new ProgressHandler() {
						@Override
						public void progress(ProgressMessage message) throws DockerException {
							final String imageId = message.buildImageId();
							if (imageId != null) {
								logger.info("Succeeded in building {}", imageId);
								try {
									logger.info("Tagging {}...", escapedTagName);
									dockerClient.tag(escapedTagName, TAG_PREFIX.concat(escapedTagName));
									logger.info("Pushing {}...", escapedTagName);
									dockerClient.push(TAG_PREFIX.concat(escapedTagName), auth);
									logger.info("Successfully pushed!");
								} catch (InterruptedException e) {
									logger.error("Thread interrupted while building", e);
									Thread.currentThread().interrupt();
								}
							}
						}
					});
		} catch (DockerException | IOException e) {
			logger.error("Docker build failed", e);
		} catch (InterruptedException e) {
			logger.error("Thread interrupted while building", e);
			Thread.currentThread().interrupt();
		}

		return TAG_PREFIX.concat(escapedTagName);
	}
}
