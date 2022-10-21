package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Requester {

	@Field(type = FieldType.Keyword)
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Requester name(String name) {
		setName(name);
		return this;
	}
}
