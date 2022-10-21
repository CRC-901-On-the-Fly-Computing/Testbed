package de.upb.crc901.otftestbed.kubernetes_spawner.impl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.kubernetes_spawner.generated.spring_server.api.ExecutorSpawnerApi;
import de.upb.crc901.otftestbed.kubernetes_spawner.generated.spring_server.api.ExecutorSpawnerApiDelegate;

@Controller
public class SpawnerController implements ExecutorSpawnerApi {

	private final SpawnerControllerDelegate delegate;

	@Autowired
	public SpawnerController(SpawnerControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public ExecutorSpawnerApiDelegate getDelegate() {
		return delegate;
	}
}
