package de.upb.crc901.otftestbed.commons.telemetry.utils;

public enum AuthenticationType {
	BUY, REVIEW;

	public String toLowerCase() {
		return name().toLowerCase();
	}
}
