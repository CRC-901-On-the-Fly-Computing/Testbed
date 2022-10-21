package de.upb.crc901.otftestbed.register.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.register.generated.spring_server.api.UserCreatorApi;
import de.upb.crc901.otftestbed.register.generated.spring_server.api.UserCreatorApiDelegate;

@Controller
public class UserCreatorController implements UserCreatorApi {

	private final UserCreatorControllerDelegate delegate;

	@Autowired
	public UserCreatorController(UserCreatorControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public UserCreatorApiDelegate getDelegate() {
		return delegate;
	}
}
