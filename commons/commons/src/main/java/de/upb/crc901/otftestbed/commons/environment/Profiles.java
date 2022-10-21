package de.upb.crc901.otftestbed.commons.environment;

public enum Profiles {
	OFFLINE,
	CLUSTER;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
