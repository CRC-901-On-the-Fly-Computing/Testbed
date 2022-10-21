package de.upb.crc901.otftestbed.commons.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public class ProsecoProcessConfiguration {

	private static final String K_ICPL_GATEWAY_HOST = "icpl.gateway.host";

	private static final String K_ICPL_GATEWAY_PORT = "icpl.gateway.port";

	private static final String K_ICPL_B3_URL = "icpl.verification.url";

	private static final String K_DISCOVERY_ADAPTER_URL = "discovery.adapter.remote.url";

	private static final String K_ATI_PI_URL = "testbed.services.accesstokennissuer";

	private static final String K_PP_URL = "testbed.services.publicparameters";

	private static final String K_PI_URL = "testbed.services.publicidentity";

	private static final String K_ELASTIC_BASE_URL = "testbed.services.elastic.base";

	private static final String K_ELASTIC_KIBANA_URL = "testbed.services.elastic.kibana";

	private String gatewayHost, gatewayPort, verificationUrl, serviceDatabaseUrl, atiPIUrl, ppUrl, piUrl,
			elasticBaseUrl, elasticKibanaUrl;

	public ProsecoProcessConfiguration() {
	}

	/**
	 * Creates a configuration for a proseco run.
	 * 
	 * @param gatewayHost        the host of the execution gateway
	 * @param gatewayPort        the port of the execution gateway
	 * @param verificationUrl    the host of the b3 composition analysis service
	 * @param serviceDatabaseUrl the host of the service database (REST-interface)
	 * @param atiPIUrl           the GET url for the access token issuer public
	 *                           identity, which resides in the buy-processor
	 * @param ppUrl              the GET url for the system's public parameters,
	 *                           which reside in the system-manager
	 * @param piUrl              the GET url for the system's public identity, which
	 *                           reside in the system-manager
	 * @param rmqHost            the host of the market-provider's rabbitMQ, which
	 *                           is being used to announce the booting process of
	 *                           the resulting service
	 * @param rmqPort            the port of the rabbitMQ
	 * @param elasticBaseUrl     the host of the elastic search, used for the
	 *                           visualization of the search
	 * @param elasticKibanaUrl   the host of the kibana dashboard
	 */
	public ProsecoProcessConfiguration(String gatewayHost, String gatewayPort, String verificationUrl,
			String serviceDatabaseUrl, String atiPIUrl, String ppUrl, String piUrl, String elasticBaseUrl,
			String elasticKibanaUrl) {
		super();
		this.gatewayHost = gatewayHost;
		this.gatewayPort = gatewayPort;
		this.verificationUrl = verificationUrl;
		this.serviceDatabaseUrl = serviceDatabaseUrl;
		this.atiPIUrl = atiPIUrl;
		this.ppUrl = ppUrl;
		this.piUrl = piUrl;
		this.elasticBaseUrl = elasticBaseUrl;
		this.elasticKibanaUrl = elasticKibanaUrl;
	}

	@BsonIgnore
	public static ProsecoProcessConfiguration getOfflineConfiguration() {
		return new ProsecoProcessConfiguration("sfb-k8master-1.cs.uni-paderborn.de", "30370",
				"http://sfb-k8master-1.cs.uni-paderborn.de:30186/api/composition_analysis/flow/withError",
				"http://sfb-k8master-1.cs.uni-paderborn.de:30301/service_registry?service_id=eq.",
				"http://sfb-k8master-1.cs.uni-paderborn.de:31755/api/access_token_issuer/public_identity",
				"http://sfb-k8master-1.cs.uni-paderborn.de:32271/api/public_parameters",
				"http://sfb-k8master-1.cs.uni-paderborn.de:32271/api/public_identity",
				"http://sfb-k8master-1.cs.uni-paderborn.de:31942",
				"http://sfb-k8master-1.cs.uni-paderborn.de:30080/elastic/kibana/app/kibana#/dashboard/");
	}

	@BsonIgnore
	public static ProsecoProcessConfiguration getKubernetesConfiguration(String otfProviderNamespace,
			String marketProviderNamespace, String elasticSearchNamespace, String executionNamespace) {
		return new ProsecoProcessConfiguration("execution-gateway." + executionNamespace, "8080",
				"http://composition-analysis." + otfProviderNamespace
						+ ":8080/api/composition_analysis/flow/withError",
				"http://sfb-k8master-1.cs.upb.de:30301/service_registry?service_id=eq.",
				"http://buy-processor." + marketProviderNamespace + ":8080/api/access_token_issuer/public_identity",
				"http://system-manager." + marketProviderNamespace + ":8080/api/public_parameters",
				"http://system-manager." + marketProviderNamespace + ":8080/api/public_identity",

				"http://esdb-data." + elasticSearchNamespace + ":9200",
				"http://elastic-kibana." + elasticSearchNamespace + ":5601/app/kibana#/dashboard/");
	}

	public void toProperties(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		Properties prop = new Properties();
		prop.put(K_ICPL_GATEWAY_HOST, gatewayHost);
		prop.put(K_ICPL_GATEWAY_PORT, gatewayPort);
		prop.put(K_ICPL_B3_URL, verificationUrl);
		prop.put(K_DISCOVERY_ADAPTER_URL, serviceDatabaseUrl);
		prop.put(K_ATI_PI_URL, atiPIUrl);
		prop.put(K_PP_URL, ppUrl);
		prop.put(K_PI_URL, piUrl);
		prop.put(K_ELASTIC_BASE_URL, elasticBaseUrl);
		prop.put(K_ELASTIC_KIBANA_URL, elasticKibanaUrl);

		PrintWriter pw = new PrintWriter(fos);
		for (Enumeration<?> e = prop.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			pw.println(key + "=" + prop.getProperty(key));
		}
		pw.close();
		fos.close();
	}

	public String getGatewayHost() {
		return gatewayHost;
	}

	public String getGatewayPort() {
		return gatewayPort;
	}

	public String getVerificationUrl() {
		return verificationUrl;
	}

	public String getServiceDatabaseUrl() {
		return serviceDatabaseUrl;
	}

	public String getAtiPIUrl() {
		return atiPIUrl;
	}

	public String getPpUrl() {
		return ppUrl;
	}

	public String getPiUrl() {
		return piUrl;
	}

	public String getElasticBaseUrl() {
		return elasticBaseUrl;
	}

	public String getElasticKibanaUrl() {
		return elasticKibanaUrl;
	}

	public void setGatewayHost(String gatewayHost) {
		this.gatewayHost = gatewayHost;
	}

	public void setGatewayPort(String gatewayPort) {
		this.gatewayPort = gatewayPort;
	}

	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}

	public void setServiceDatabaseUrl(String serviceDatabaseUrl) {
		this.serviceDatabaseUrl = serviceDatabaseUrl;
	}

	public void setAtiPIUrl(String atiPIUrl) {
		this.atiPIUrl = atiPIUrl;
	}

	public void setPpUrl(String ppUrl) {
		this.ppUrl = ppUrl;
	}

	public void setPiUrl(String piUrl) {
		this.piUrl = piUrl;
	}

	public void setElasticBaseUrl(String elasticBaseUrl) {
		this.elasticBaseUrl = elasticBaseUrl;
	}

	public void setElasticKibanaUrl(String elasticKibanaUrl) {
		this.elasticKibanaUrl = elasticKibanaUrl;
	}

}
