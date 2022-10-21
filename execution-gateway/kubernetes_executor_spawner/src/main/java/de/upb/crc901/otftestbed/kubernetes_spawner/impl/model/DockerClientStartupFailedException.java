package de.upb.crc901.otftestbed.kubernetes_spawner.impl.model;

import com.spotify.docker.client.exceptions.DockerCertificateException;

public class DockerClientStartupFailedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DockerClientStartupFailedException() {
	}
	
	public DockerClientStartupFailedException (DockerCertificateException e) {
		super (e);
	}

}
