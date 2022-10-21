package de.upb.crc901.otftestbed.commons.connect;

import de.upb.crc901.otftestbed.commons.config.ConfigFileURLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Resolver for the api clients base paths out of configuration files.
 *
 * @author Roman
 */
@Component
@ComponentScan({"de.upb.crc901.otftestbed.commons.config"})
public class ConfigFileResolver extends BasePathResolver {

	@Autowired
	private ConfigFileURLResolver urlResolver;

	/**
	 * Generate the base path for the given {@link PocService}.
	 * @param service to generate the base path for
	 * @return the base path for the given {@link PocService}
	 */
	@Override
	public String createBasePath(PocService service) {
		switch (service) {

			case ADMIN_CLIENT:
				return "";

			case LOCAL_SERVICE_REQUESTER:
				return "";
			case LOCAL_USER_CREATOR:
				return "";

			case MP_BUY_PROCESSOR:
				return this.urlResolver.getBuyProcessorBaseURL();
			case MP_CREDENTIAL_ISSUER:
				return this.urlResolver.getCredentialIssuerBaseURL();
			case MP_OTFP_REGISTRY:
				return this.urlResolver.getOtfRegistryBaseURL();
			case MP_REVIEW_BOARD:
				return this.urlResolver.getReviewBoardBaseURL();
			case MP_SYSTEM_MANAGER:
				return this.urlResolver.getSystemManagerBaseURL();

			case OP_COMPOSITION_ANALYSIS:
				return this.urlResolver.getCompositionAnalysisBaseURL();
			case OP_GATEKEEPER:
				return this.urlResolver.getGateKeeperBaseURL();
			case OP_POLICY_PROVIDER:
				return this.urlResolver.getPolicyProviderBaseURL();
			case OP_PROSECO_CONFIGURATOR:
				return this.urlResolver.getProsecoBaseURL();

			default:
				return null;
		}
	}
}
