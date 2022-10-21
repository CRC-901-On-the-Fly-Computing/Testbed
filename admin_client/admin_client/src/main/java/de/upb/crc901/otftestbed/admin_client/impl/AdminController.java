package de.upb.crc901.otftestbed.admin_client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.admin_client.generated.spring_server.api.AdminApi;
import de.upb.crc901.otftestbed.admin_client.generated.spring_server.api.AdminApiDelegate;

@Controller
public class AdminController implements AdminApi {

	private final AdminControllerDelegate delegate;

	@Autowired
	public AdminController(AdminControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public AdminApiDelegate getDelegate() {
		return delegate;
	}
}
