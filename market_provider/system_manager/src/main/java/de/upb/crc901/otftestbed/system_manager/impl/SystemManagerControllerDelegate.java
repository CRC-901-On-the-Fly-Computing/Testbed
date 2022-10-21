package de.upb.crc901.otftestbed.system_manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.system_manager.generated.spring_server.api.SystemManagerApiDelegate;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactJoinResponse;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractiveJoinRequest;

@Component
public class SystemManagerControllerDelegate implements SystemManagerApiDelegate {

	private SystemManagerComponent systemManager;


	@Autowired
	public SystemManagerControllerDelegate(SystemManagerComponent systemManager) {
		Assert.notNull(systemManager, "systemManager must not be null");

		this.systemManager = systemManager;
	}


	@Override
	public ResponseEntity<ReactJoinResponse> joinUser(ReactNonInteractiveJoinRequest body) {
		ReactJoinResponse joinResponse = systemManager.getSystemManager().nonInteractiveJoinVerification(body);
		return ResponseEntity.ok(joinResponse);
	}

	@Override
	public ResponseEntity<ReactSystemManagerPublicIdentity> getPublicIdentity() {
		return ResponseEntity.ok(systemManager.getSystemManager().getPublicIdentity());
	}

	@Override
	public ResponseEntity<ReactPublicParameters> getPublicParameter() {
		return ResponseEntity.ok(systemManager.getPublicParameters());
	}
}
