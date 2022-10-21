package de.upb.crc901.otftestbed.commons.rest;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleJSONUuid {
	
	@JsonProperty
	private UUID uuid;
	
	public SimpleJSONUuid() {}

	public SimpleJSONUuid(UUID uuid) {
		super();
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "SimpleJSONUuid [uuid=" + uuid + "]";
	}

}
