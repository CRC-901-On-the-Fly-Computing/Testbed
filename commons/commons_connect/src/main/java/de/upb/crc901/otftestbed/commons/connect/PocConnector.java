package de.upb.crc901.otftestbed.commons.connect;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Basic class for the connection between the PoC services.
 *
 * @author Roman
 */
@Component
public class PocConnector {

	@JsonProperty
	private MarketProviderConnector marketProviderConnector;

	@JsonProperty
	private OtfProviderConnector otfProviderConnector;


	@Autowired
	public PocConnector(MarketProviderConnector marketProviderConnector, OtfProviderConnector otfProviderConnector,
				BasePathResolver resolver) {
		this.marketProviderConnector = marketProviderConnector;
		this.otfProviderConnector = otfProviderConnector;

		this.marketProviderConnector.setBasePathResolver(resolver);
		this.otfProviderConnector.setBasePathResolver(resolver);
	}


	/**
	 * Get the {@link MarketProviderConnector} object.
	 * @return the {@link MarketProviderConnector} object.
	 */
	public MarketProviderConnector toMarketProvider() {
		return this.marketProviderConnector;
	}

	/**
	 * Get the {@link OtfProviderConnector} object.
	 * @return the {@link OtfProviderConnector} object.
	 */
	public OtfProviderConnector toOtfProvider() {
		return this.otfProviderConnector;
	}
}
