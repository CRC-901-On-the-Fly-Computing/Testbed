package de.upb.crc901.otftestbed.commons.connect;

import org.springframework.stereotype.Component;

@Component
public abstract class BasePathResolver {

	private ProviderConnector providerConnector;


	public BasePathResolver() {
	}

	public BasePathResolver(ProviderConnector connector) {
		setProviderConnector(connector);
	}


	/**
	 * Get the {@link ProviderConnector}.
	 * @return the {@link ProviderConnector}
	 */
	public ProviderConnector getProviderConnector() {
		return this.providerConnector;
	}

	/**
	 * Sets new {@link ProviderConnector} and disconnects from previous one.
	 * @param connector new {@link ProviderConnector}
	 */
	public void setProviderConnector(ProviderConnector connector) {
		if (this.providerConnector == connector) {
			return;
		}

		if (this.providerConnector != null) {
			ProviderConnector old = this.providerConnector;
			this.providerConnector = null;
			old.setBasePathResolver(null);
		}

		this.providerConnector = connector;

		if (connector != null) {
			connector.setBasePathResolver(this);
		}
	}

	/**
	 * Incite the provider to refresh his base paths.
	 */
	public void refresh() {
		this.providerConnector.refresh();
	}

	/**
	 * Generate the base path for the given {@link PocService}.
	 * @param service to generate the base path for
	 * @return the base path for the given {@link PocService}
	 */
	public abstract String createBasePath(PocService service);
}
