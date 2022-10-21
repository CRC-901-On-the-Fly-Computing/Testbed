package de.upb.crc901.otftestbed.proseco.impl.config;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;

/**
 * Simple configuration class that contains the keys of the environment
 * variables.
 * 
 * @author Mirko Jürgens
 *
 */
@Configuration
public class EnvironmentVariableConfiguration {

	/* Specifies the environment variable that contains the OTF-provider uuid */
	public static final String OTFP_UUID_ENV_KEY = "OTFP_UUID";
	
	/* Specifies the environment variable that contains the OTF-provider name */
	public static final String OTFP_NAME_ENV_KEY = "OTFP_NAME";

	/* Specifies the environment variable that contains the OTF-provider icon url */
	public static final String OTFP_ICON_ENV_KEY = "OTFP_ICON";
	
	/*
	 * Specifies the environment variable that contains the port of the request
	 * database
	 */
	public static final String REQUEST_DB_PORT_ENV_KEY = "REQUEST_DB_PORT";

	/*
	 * Specifies the environment variable that contains the host of the request
	 * database
	 */
	public static final String REQUEST_DB_HOST_ENV_KEY = "REQUEST_DB_HOST";

	/*
	 * Specifies the environment variable that describes the kube-secret in the
	 * pod-namespace, which contains the credentials for the nexus registry
	 */
	public static final String DOCKER_SECRET_ENV_KEY = "DOCKER_SECRET_NAME";

	/*
	 * Specifies the environment variable that describes the kube-secret in the
	 * pod-namespace, which contains the access-credentials for the request database
	 */
	public static final String REQUEST_SECRET_ENV_KEY = "REQUEST_SECRET_NAME";

	/*
	 * Specifies the environment variable that describes the namespace of the pods.
	 */
	public static final String POD_NAMESPACE_ENV_KEY = "POD_NAMESPACE";

	/*
	 * Specifies the environment variable that describes the namespace of the
	 * market-provider.
	 */
	public static final String MARKET_PROVIDER_NAMESPACE_ENV_KEY = "MARKET_PROVIDER_NAMESPACE";

	/*
	 * Specifies the environment variable that describes the namespace of the
	 * market-provider.
	 */
	public static final String EXECUTION_NAMESPACE_ENV_KEY = "EXECUTION_NAMESPACE";

	/*
	 * Specifies the environment variable that describes the namespace of this
	 * otf-provider.
	 */
	public static final String OTFP_NAMESPACE_ENV_KEY = "OTFP_NAMESPACE";

	/*
	 * Specifies the environment variable that describes the namespace of the
	 * elastic-search.
	 */
	public static final String ELASTIC_NAMESPACE_ENV_KEY = "ELASTIC_NAMESPACE";

	/*
	 * Specifies the environment variable that describes the namespace of the
	 * execution.
	 */
	public static final String DOCKER_CREDENTIAL_SECRET_NAME_ENV_KEY = "DOCKER_CREDENTIAL_SECRET_NAME";

	private final UUID otfpUUID;
	
	private final String otfpIcon = System.getenv(OTFP_ICON_ENV_KEY);
	
	private final String otfpName = System.getenv(OTFP_NAME_ENV_KEY);

	private final String dockerSecretName = System.getenv(DOCKER_SECRET_ENV_KEY);

	private final String requestDBSecretName = System.getenv(REQUEST_SECRET_ENV_KEY);

	private final String podNamespace = System.getenv(POD_NAMESPACE_ENV_KEY);

	private final String marketProviderNamespace = System.getenv(MARKET_PROVIDER_NAMESPACE_ENV_KEY);

	private final String executionNamespace = System.getenv(EXECUTION_NAMESPACE_ENV_KEY);

	private final String elasticNamespace = System.getenv(ELASTIC_NAMESPACE_ENV_KEY);

	private final String otfpNamespace = System.getenv(OTFP_NAMESPACE_ENV_KEY);

	private final String dockerCredentialsecretName = System.getenv(DOCKER_CREDENTIAL_SECRET_NAME_ENV_KEY);

	private final String requestDBPort = System.getenv(REQUEST_DB_PORT_ENV_KEY);

	private final String dbHost;

	public EnvironmentVariableConfiguration() {
		String dbHostReference = System.getenv(REQUEST_DB_HOST_ENV_KEY);
		dbHost = System.getenv(dbHostReference);
		otfpUUID = UUID.nameUUIDFromBytes(otfpName.getBytes(StandardCharsets.UTF_8));
	}

	public UUID getOtfpUUID() {
		return otfpUUID;
	}

	public String getDockerSecretName() {
		return dockerSecretName;
	}

	public String getRequestDBSecretName() {
		return requestDBSecretName;
	}

	public String getPodNamespace() {
		return podNamespace;
	}

	public String getMarketProviderNamespace() {
		return marketProviderNamespace;
	}

	public String getExecutionNamespace() {
		return executionNamespace;
	}

	public String getElasticNamespace() {
		return elasticNamespace;
	}

	public String getOtfpNamespace() {
		return otfpNamespace;
	}

	public String getDockerCredentialsecretName() {
		return dockerCredentialsecretName;
	}

	public String getRequestDBPort() {
		return requestDBPort;
	}

	public String getDbHost() {
		return dbHost;
	}

	public String getOtfpIcon() {
		return otfpIcon;
	}

	public String getOtfpName() {
		return otfpName;
	}
}
