package de.upb.crc901.otftestbed.commons.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Detector {

	private static final Logger log = LoggerFactory.getLogger(Detector.class);


	public static Profiles detectProfile() {
		try {
			InetAddress.getByName("kubernetes.default");

			log.debug("Cluster deployment detected.");
			return Profiles.CLUSTER;

		} catch (UnknownHostException e) {

			log.warn("Local deployment detected (kubernetes-dns was unreachable).");
			return Profiles.OFFLINE;
		}
	}
}
