package de.upb.crc901.otftestbed.commons.connect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.buy_processor.generated.java_client.api.BuyProcessorApi;
import de.upb.crc901.otftestbed.credential_issuer.generated.java_client.api.CredentialIssuerApi;
import de.upb.crc901.otftestbed.registry.generated.java_client.api.ProviderRegistryApi;
import de.upb.crc901.otftestbed.review_board.generated.java_client.api.ReviewBoardApi;
import de.upb.crc901.otftestbed.system_manager.generated.java_client.api.SystemManagerApi;

/**
 * Collection of the api client objects for the market provider.
 *
 * @author Roman
 */
@Component
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.buy_processor.generated.java_client",
		"de.upb.crc901.otftestbed.credential_issuer.generated.java_client",
		"de.upb.crc901.otftestbed.registry.generated.java_client",
		"de.upb.crc901.otftestbed.review_board.generated.java_client",
		"de.upb.crc901.otftestbed.system_manager.generated.java_client",
		"de.upb.crc901.otftestbed.postgrest.generated.java_client"
})
public class MarketProviderConnector extends ProviderConnector {

	/**
	 * The logger
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(MarketProviderConnector.class);

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private BuyProcessorApi buyProcessorApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private CredentialIssuerApi credentialIssuerApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private ProviderRegistryApi otfpRegistryApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private ReviewBoardApi reviewBoardApi;

	@JsonIgnore // ignore api client object to de-/serialize only the base path
	private SystemManagerApi systemManagerApi;


	@Autowired
	public MarketProviderConnector(BuyProcessorApi buyProcessorApi, CredentialIssuerApi credentialIssuerApi,
				ProviderRegistryApi otfpRegistryApi, ReviewBoardApi reviewBoardApi, SystemManagerApi systemManagerApi) {

		this.buyProcessorApi = buyProcessorApi;
		this.credentialIssuerApi = credentialIssuerApi;
		this.otfpRegistryApi = otfpRegistryApi;
		this.reviewBoardApi = reviewBoardApi;
		this.systemManagerApi = systemManagerApi;
	}


	/**
	 * Get the {@link BuyProcessorApi} client api object.
	 * @return the {@link BuyProcessorApi} client api object.
	 */
	public BuyProcessorApi callBuyProcessor() {
		return this.buyProcessorApi;
	}

	/**
	 * Get the base path of the {@link BuyProcessorApi} client api object.
	 * @return the base path
	 */
	public String getBuyProcessorBasePath() {
		return this.buyProcessorApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link BuyProcessorApi} client api object.
	 * @param basePath to be set
	 */
	public void setBuyProcessorBasePath(String basePath) {
		this.buyProcessorApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link CredentialIssuerApi} client api object.
	 * @return the {@link CredentialIssuerApi} client api object.
	 */
	public CredentialIssuerApi callCredentialIssuer() {
		return this.credentialIssuerApi;
	}

	/**
	 * Get the base path of the {@link CredentialIssuerApi} client api object.
	 * @return the base path
	 */
	public String getCredentialIssuerBasePath() {
		return this.credentialIssuerApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link CredentialIssuerApi} client api object.
	 * @param basePath to be set
	 */
	public void setCredentialIssuerBasePath(String basePath) {
		this.credentialIssuerApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link ProviderRegistryApi} client api object.
	 * @return the {@link ProviderRegistryApi} client api object.
	 */
	public ProviderRegistryApi callOtfpRegistry() {
		return this.otfpRegistryApi;
	}

	/**
	 * Get the base path of the {@link ProviderRegistryApi} client api object.
	 * @return the base path
	 */
	public String getOtfpRegistryBasePath() {
		return this.otfpRegistryApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link ProviderRegistryApi} client api object.
	 * @param basePath to be set
	 */
	public void setOtfpRegistryBasePath(String basePath) {
		this.otfpRegistryApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link ReviewBoardApi} client api object.
	 * @return the {@link ReviewBoardApi} client api object.
	 */
	public ReviewBoardApi callReviewBoard() {
		return this.reviewBoardApi;
	}

	/**
	 * Get the base path of the {@link ReviewBoardApi} client api object.
	 * @return the base path
	 */
	public String getReviewBoardBasePath() {
		return this.reviewBoardApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link ReviewBoardApi} client api object.
	 * @param basePath to be set
	 */
	public void setReviewBoardBasePath(String basePath) {
		this.reviewBoardApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Get the {@link SystemManagerApi} client api object.
	 * @return the {@link SystemManagerApi} client api object.
	 */
	public SystemManagerApi callSystemManager() {
		return this.systemManagerApi;
	}

	/**
	 * Get the base path of the {@link SystemManagerApi} client api object.
	 * @return the base path
	 */
	public String getSystemManagerBasePath() {
		return this.systemManagerApi.getApiClient().getBasePath();
	}

	/**
	 * Set the base path of the {@link SystemManagerApi} client api object.
	 * @param basePath to be set
	 */
	public void setSystemManagerBasePath(String basePath) {
		this.systemManagerApi.getApiClient().setBasePath(basePath);
	}


	/**
	 * Refreshes the base paths of the api client objects.
	 */
	@Override
	public void refresh() {
		log.debug("Updating base paths.");

		setBuyProcessorBasePath(generateBasePath(PocService.MP_BUY_PROCESSOR));
		setCredentialIssuerBasePath(generateBasePath(PocService.MP_CREDENTIAL_ISSUER));
		setOtfpRegistryBasePath(generateBasePath(PocService.MP_OTFP_REGISTRY));
		setReviewBoardBasePath(generateBasePath(PocService.MP_REVIEW_BOARD));
		setSystemManagerBasePath(generateBasePath(PocService.MP_SYSTEM_MANAGER));
	}
}
