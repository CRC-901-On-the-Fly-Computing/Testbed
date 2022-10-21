package de.upb.crc901.otftestbed.credential_issuer.impl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.credential_issuer.generated.spring_server.api.CredentialIssuerApi;
import de.upb.crc901.otftestbed.credential_issuer.generated.spring_server.api.CredentialIssuerApiDelegate;

@Controller
public class CredentialIssuerController implements CredentialIssuerApi {

	private final CredentialIssuerControllerDelegate delegate;

	@Autowired
	public CredentialIssuerController(CredentialIssuerControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public CredentialIssuerApiDelegate getDelegate() {
		return delegate;
	}
}
