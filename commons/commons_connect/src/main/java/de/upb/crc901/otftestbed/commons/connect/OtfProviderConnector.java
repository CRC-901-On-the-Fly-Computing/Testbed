package de.upb.crc901.otftestbed.commons.connect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.java_client.api.GatekeeperApi;
import de.upb.crc901.otftestbed.policy_provider.generated.java_client.api.PolicyProviderApi;
import de.upb.crc901.otftestbed.proseco.generated.java_client.api.ProsecoApi;
import de.upb.crc901.otftestbed.verificator.generated.java_client.api.CompositionAnalysisApi;

/**
 * Collection of the api client objects for the otf provider.
 *
 * @author Roman
 */
@Component
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.java_client",
		"de.upb.crc901.otftestbed.policy_provider.generated.java_client",
		"de.upb.crc901.otftestbed.proseco.generated.java_client",
		"de.upb.crc901.otftestbed.verificator.generated.java_client"
})
public class OtfProviderConnector extends ProviderConnector {

	/**
	 * The logger
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(OtfProviderConnector.class);

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private CompositionAnalysisApi compositionAnalysisApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private GatekeeperApi gatekeeperApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private PolicyProviderApi policyProviderApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private ProsecoApi prosecoConfiguratorApi;


	@Autowired
	public OtfProviderConnector(CompositionAnalysisApi compositionAnalysisApi, GatekeeperApi gatekeeperApi,
				PolicyProviderApi policyProviderApi, ProsecoApi prosecoConfiguratorApi) {

		this.compositionAnalysisApi = compositionAnalysisApi;
		this.gatekeeperApi = gatekeeperApi;
		this.policyProviderApi = policyProviderApi;
		this.prosecoConfiguratorApi = prosecoConfiguratorApi;
	}


	/**
	 * Get the {@link CompositionAnalysisApi} client api object.
	 * @return the {@link CompositionAnalysisApi} client api object.
	 */
	public CompositionAnalysisApi callCompositionAnalysis() {
		return this.compositionAnalysisApi;
	}

	/**
	 * Get the {@link CompositionAnalysisApi} client api object and set the given base path.
	 * @param basePath to send request to
	 * @return the {@link CompositionAnalysisApi} client api object.
	 */
	public CompositionAnalysisApi callCompositionAnalysis(String basePath) {
		setCompositionAnalysisBasePath((basePath != null ? basePath : generateBasePath(PocService.OP_COMPOSITION_ANALYSIS)));
		return this.compositionAnalysisApi;
	}

	/**
	 * Get the base path of the {@link CompositionAnalysisApi} client api object.
	 * @return the base path
	 */
	public String getCompositionAnalysisBasePath() {
		return this.compositionAnalysisApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link CompositionAnalysisApi} client api object.
	 * @param basePath to be set
	 */
	public void setCompositionAnalysisBasePath(String basePath) {
		this.compositionAnalysisApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link GatekeeperApi} client api object.
	 * @return the {@link GatekeeperApi} client api object.
	 */
	public GatekeeperApi callGatekeeper() {
		return this.gatekeeperApi;
	}

	/**
	 * Get the {@link GatekeeperApi} client api object and set the given base path.
	 * @param basePath to send request to
	 * @return the {@link GatekeeperApi} client api object.
	 */
	public GatekeeperApi callGatekeeper(String basePath) {
		setGatekeeperBasePath((basePath != null ? basePath : generateBasePath(PocService.OP_GATEKEEPER)));
		return this.gatekeeperApi;
	}

	/**
	 * Get the base path of the {@link GatekeeperApi} client api object.
	 * @return the base path
	 */
	public String getGatekeeperBasePath() {
		return this.gatekeeperApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link GatekeeperApi} client api object.
	 * @param basePath to be set
	 */
	public void setGatekeeperBasePath(String basePath) {
		this.gatekeeperApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link PolicyProviderApi} client api object.
	 * @return the {@link PolicyProviderApi} client api object.
	 */
	public PolicyProviderApi callPolicyProvider() {
		return this.policyProviderApi;
	}

	/**
	 * Get the {@link PolicyProviderApi} client api object and set the given base path.
	 * @param basePath to send request to
	 * @return the {@link PolicyProviderApi} client api object.
	 */
	public PolicyProviderApi callPolicyProvider(String basePath) {
		setPolicyProviderBasePath((basePath != null ? basePath : generateBasePath(PocService.OP_POLICY_PROVIDER)));
		return this.policyProviderApi;
	}

	/**
	 * Get the base path of the {@link PolicyProviderApi} client api object.
	 * @return the base path
	 */
	public String getPolicyProviderBasePath() {
		return this.policyProviderApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link PolicyProviderApi} client api object.
	 * @param basePath to be set
	 */
	public void setPolicyProviderBasePath(String basePath) {
		this.policyProviderApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link ProsecoApi} client api object.
	 * @return the {@link ProsecoApi} client api object.
	 */
	public ProsecoApi callProsecoConfigurator() {
		return this.prosecoConfiguratorApi;
	}

	/**
	 * Get the {@link ProsecoApi} client api object and set the given base path.
	 * @param basePath to send request to
	 * @return the {@link ProsecoApi} client api object.
	 */
	public ProsecoApi callProsecoConfigurator(String basePath) {
		setProsecoConfiguratorBasePath((basePath != null ? basePath : generateBasePath(PocService.OP_PROSECO_CONFIGURATOR)));
		return this.prosecoConfiguratorApi;
	}

	/**
	 * Get the base path of the {@link ProsecoApi} client api object.
	 * @return the base path
	 */
	public String getProsecoConfiguratorBasePath() {
		return this.prosecoConfiguratorApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link ProsecoApi} client api object.
	 * @param basePath to be set
	 */
	public void setProsecoConfiguratorBasePath(String basePath) {
		this.prosecoConfiguratorApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Refreshes the base paths of the api client objects.
	 */
	@Override
	public void refresh() {
		log.debug("Updating base paths.");

		setCompositionAnalysisBasePath(generateBasePath(PocService.OP_COMPOSITION_ANALYSIS));
		setGatekeeperBasePath(generateBasePath(PocService.OP_GATEKEEPER));
		setPolicyProviderBasePath(generateBasePath(PocService.OP_POLICY_PROVIDER));
		setProsecoConfiguratorBasePath(generateBasePath(PocService.OP_PROSECO_CONFIGURATOR));
	}
}
