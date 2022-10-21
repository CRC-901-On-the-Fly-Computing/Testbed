package de.upb.crc901.otftestbed.system_manager.impl;

import de.upb.crc901.otftestbed.commons.io.RepresentationFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParametersFactory;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManager;

@Component
public class SystemManagerComponent {

	private static final Logger log = LoggerFactory.getLogger(SystemManagerComponent.class);

	private final SystemManagerConfig systemManagerConfig;

	private ReactSystemManager systemManager;

	private ReactPublicParameters publicParameters;


	@Autowired
	public SystemManagerComponent(SystemManagerConfig systemManagerConfig) {
		Assert.notNull(systemManagerConfig, "systemManagerConfig must not be null");

		this.systemManagerConfig = systemManagerConfig;

		init();
	}


	private void init() {
		// initialization

		// check whether we use an already existent version
		if (systemManagerConfig.isPersistent()) {
			log.debug("Trying to recreate...");

			this.publicParameters = RepresentationFile.readFile(
					SystemManagerConfig.PUBLIC_PARAMETERS_FILENAME, ReactPublicParameters::new);
			this.systemManager = RepresentationFile.readFile(
					SystemManagerConfig.SYSMANAGER_FILENAME, ReactSystemManager::new);

		} else {
			ReactPublicParametersFactory setup = new ReactPublicParametersFactory();
			setup.setDebugMode(true);

			this.publicParameters = setup.create();
			this.systemManager = new ReactSystemManager(this.publicParameters);
		}
	}

	public ReactSystemManager getSystemManager() {
		return this.systemManager;
	}

	public ReactPublicParameters getPublicParameters() {
		return this.publicParameters;
	}
}
