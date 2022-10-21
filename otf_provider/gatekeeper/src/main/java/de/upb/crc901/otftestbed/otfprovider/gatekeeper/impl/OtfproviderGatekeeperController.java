package de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.spring_server.api.GatekeeperApi;
import de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.spring_server.api.GatekeeperApiDelegate;

@Controller
@ComponentScan(basePackages = "de.upb.crc901.otftestbed.commons.config")
public class OtfproviderGatekeeperController implements GatekeeperApi {

	private final OtfproviderGatekeeperDelegate delegate;

	@Autowired
	public OtfproviderGatekeeperController(OtfproviderGatekeeperDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public GatekeeperApiDelegate getDelegate() {
		return delegate;
	}
}
