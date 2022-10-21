package de.upb.crc901.otftestbed.service_requester.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.service_requester.generated.spring_server.api.ServiceRequesterApi;
import de.upb.crc901.otftestbed.service_requester.generated.spring_server.api.ServiceRequesterApiDelegate;

@Controller
public class ServiceRequesterController implements ServiceRequesterApi {

	private final ServiceRequesterControllerDelegate delegate;

	@Autowired
	public ServiceRequesterController(ServiceRequesterControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public ServiceRequesterApiDelegate getDelegate() {
		return delegate;
	}
}
