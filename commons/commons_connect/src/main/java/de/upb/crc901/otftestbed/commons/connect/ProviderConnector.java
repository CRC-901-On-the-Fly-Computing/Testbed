package de.upb.crc901.otftestbed.commons.connect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

@Component
public abstract class ProviderConnector {

	@JsonIgnore // ignore resolver, because this is implementation detail of distinct services
	private BasePathResolver basePathResolver;


	public ProviderConnector() {
	}

	public ProviderConnector(BasePathResolver basePathResolver) {
		setBasePathResolver(basePathResolver);
	}


	/**
	 * Get the {@link BasePathResolver}.
	 * @return the {@link BasePathResolver}
	 */
	public BasePathResolver getBasePathResolver() {
		return this.basePathResolver;
	}

	/**
	 * Sets new {@link BasePathResolver} and disconnects from previous one.
	 * @param basePathResolver new {@link BasePathResolver}
	 */
	public void setBasePathResolver(BasePathResolver basePathResolver) {
		if (this.basePathResolver == basePathResolver) {
			return;
		}

		if (this.basePathResolver != null) {
			BasePathResolver old = this.basePathResolver;
			this.basePathResolver = null;
			old.setProviderConnector(null);
		}

		this.basePathResolver = basePathResolver;

		if (basePathResolver != null) {
			basePathResolver.setProviderConnector(this);
			refresh();
		}
	}

	/**
	 * Get the base path for the given {@link PocService}.
	 * @param service to get the base path for
	 * @return the base path for the given {@link PocService}
	 */
	public String generateBasePath(PocService service) {
		return basePathResolver.createBasePath(service);
	}

	/**
	 * This method is called to refresh the base paths.
	 */
	public abstract void refresh();
}
