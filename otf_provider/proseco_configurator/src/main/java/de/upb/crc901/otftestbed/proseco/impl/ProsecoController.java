package de.upb.crc901.otftestbed.proseco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.proseco.generated.spring_server.api.ProsecoApi;
import de.upb.crc901.otftestbed.proseco.generated.spring_server.api.ProsecoApiDelegate;

@Controller
public class ProsecoController implements ProsecoApi {

	private final ProsecoApiImpl delegate;

	@Autowired
	public ProsecoController(ProsecoApiImpl delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public ProsecoApiDelegate getDelegate() {
		return delegate;
	}
}
