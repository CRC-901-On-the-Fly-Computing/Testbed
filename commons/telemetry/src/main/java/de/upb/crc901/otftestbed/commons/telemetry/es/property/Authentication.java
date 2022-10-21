package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Authentication {

	@Field(type = FieldType.Keyword)
	private String type;

	@Field(type = FieldType.Boolean)
	private Boolean success;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Authentication type(String type) {
		setType(type);
		return this;
	}


	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Authentication success(Boolean success) {
		setSuccess(success);
		return this;
	}
}
