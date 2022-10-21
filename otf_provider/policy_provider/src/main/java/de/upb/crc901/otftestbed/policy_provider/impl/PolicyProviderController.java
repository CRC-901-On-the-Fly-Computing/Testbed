package de.upb.crc901.otftestbed.policy_provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.policy_provider.generated.spring_server.api.PolicyProviderApi;
import de.upb.crc901.otftestbed.policy_provider.generated.spring_server.api.PolicyProviderApiDelegate;

@Controller
public class PolicyProviderController implements PolicyProviderApi {

	private final PolicyProviderControllerDelegate delegate;

	@Autowired
	public PolicyProviderController(PolicyProviderControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public PolicyProviderApiDelegate getDelegate() {
		return delegate;
	}
}
