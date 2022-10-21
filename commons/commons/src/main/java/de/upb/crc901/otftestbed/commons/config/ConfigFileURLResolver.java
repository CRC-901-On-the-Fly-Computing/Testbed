package de.upb.crc901.otftestbed.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/urlconfigs/config-${spring.profiles.active}.properties")
public class ConfigFileURLResolver {

	@Value("${basePath}")
	private String basePath;

	// MISC
	@Value("${b1BaseUrl}")
	private String b1BaseUrl;
	@Value("${b1PostTextSuffix}")
	private String b1PostTextSuffix;

	// Market Provider
	@Value("${buyProcessorBaseURL}")
	private String buyProcessorBaseURL;
	@Value("${credentialIssuerBaseURL}")
	private String credentialIssuerBaseURL;
	@Value("${otfRegistryBaseURL}")
	private String otfRegistryBaseURL;
	@Value("${reviewBoardBaseURL}")
	private String reviewBoardBaseURL;
	@Value("${systemManagerBaseURL}")
	private String systemManagerBaseURL;

	// OTF Provider
	@Value("${compositionAnalysisBaseURL}")
	private String compositionAnalysisBaseURL;
	@Value("${gateKeeperBaseURL}")
	private String gateKeeperBaseURL;
	@Value("${policyProviderBaseURL}")
	private String policyProviderBaseURL;
	@Value("${prosecoBaseURL}")
	private String prosecoBaseURL;


	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}


	public String getB1BaseUrl() {
		return b1BaseUrl;
	}

	public void setB1BaseUrl(String baseURL) {
		this.b1BaseUrl = baseURL;
	}

	public String getB1PostTextSuffix() {
		return b1PostTextSuffix;
	}

	public void setB1PostTextSuffix(String b1PostTextSuffix) {
		this.b1PostTextSuffix = b1PostTextSuffix;
	}


	/*
	 * Market Provider
	 */

	public String getBuyProcessorBaseURL() {
		return buyProcessorBaseURL + basePath;
	}

	public void setBuyProcessorBaseURL(String baseURL) {
		this.buyProcessorBaseURL = baseURL;
	}


	public String getCredentialIssuerBaseURL() {
		return credentialIssuerBaseURL + basePath;
	}

	public void setCredentialIssuerBaseURL(String baseURL) {
		this.credentialIssuerBaseURL = baseURL;
	}


	public String getOtfRegistryBaseURL() {
		return otfRegistryBaseURL + basePath;
	}

	public void setOtfRegistryBaseURL(String baseURL) {
		this.otfRegistryBaseURL = baseURL;
	}


	public String getReviewBoardBaseURL() {
		return reviewBoardBaseURL + basePath;
	}

	public void setReviewBoardBaseURL(String baseURL) {
		this.reviewBoardBaseURL = baseURL;
	}


	public String getSystemManagerBaseURL() {
		return systemManagerBaseURL + basePath;
	}

	public void setSystemManagerBaseURL(String baseURL) {
		this.systemManagerBaseURL = baseURL;
	}


	/*
	 * OTF Provider
	 */

	public String getCompositionAnalysisBaseURL() {
		return compositionAnalysisBaseURL + basePath;
	}

	public void setCompositionAnalysisBaseURL(String baseURL) {
		this.compositionAnalysisBaseURL = baseURL;
	}


	public String getGateKeeperBaseURL() {
		return gateKeeperBaseURL + basePath;
	}

	public void setGateKeeperBaseURL(String baseURL) {
		this.gateKeeperBaseURL = baseURL;
	}


	public String getPolicyProviderBaseURL() {
		return policyProviderBaseURL + basePath;
	}

	public void setPolicyProviderBaseURL(String baseURL) {
		this.policyProviderBaseURL = baseURL;
	}


	public String getProsecoBaseURL() {
		return prosecoBaseURL + basePath;
	}

	public void setProsecoBaseURL(String baseURL) {
		this.prosecoBaseURL = baseURL;
	}
}
