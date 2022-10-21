package de.upb.crc901.otftestbed.system_manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.system_manager.generated.spring_server.api.SystemManagerApi;
import de.upb.crc901.otftestbed.system_manager.generated.spring_server.api.SystemManagerApiDelegate;

@Controller
public class SystemManagerController implements SystemManagerApi {

	private final SystemManagerControllerDelegate delegate;

	@Autowired
	public SystemManagerController(SystemManagerControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public SystemManagerApiDelegate getDelegate() {
		return delegate;
	}
}
