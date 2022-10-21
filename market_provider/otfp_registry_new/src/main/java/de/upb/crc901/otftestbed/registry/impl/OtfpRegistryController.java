package de.upb.crc901.otftestbed.registry.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.registry.generated.spring_server.api.ProviderRegistryApi;
import de.upb.crc901.otftestbed.registry.generated.spring_server.api.ProviderRegistryApiDelegate;

@Controller
public class OtfpRegistryController implements ProviderRegistryApi {

	private final OtfpRegistryControllerDelegate delegate;

	@Autowired
	public OtfpRegistryController(OtfpRegistryControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public ProviderRegistryApiDelegate getDelegate() {
		return delegate;
	}
}
