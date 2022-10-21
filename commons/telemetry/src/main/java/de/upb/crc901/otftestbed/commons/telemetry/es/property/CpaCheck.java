package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class CpaCheck {

	@Field(type = FieldType.Keyword)
	private String servId;

	@Field(type = FieldType.Boolean)
	private Boolean isCertified;

	@Field(type = FieldType.Keyword)
	private Boolean isVerified;

	@Field(type = FieldType.Long)
	private Long size;

	@Field(type = FieldType.Text)
	private String url;


	public String getServId() {
		return servId;
	}

	public void setServId(String servId) {
		this.servId = servId;
	}

	public CpaCheck servId(String servId) {
		setServId(servId);
		return this;
	}


	public Boolean getIsCertified() {
		return isCertified;
	}

	public void setIsCertified(Boolean isCertified) {
		this.isCertified = isCertified;
	}

	public CpaCheck isCertified(Boolean isCertified) {
		setIsCertified(isCertified);
		return this;
	}


	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public CpaCheck isVerified(Boolean isVerified) {
		setIsVerified(isVerified);
		return this;
	}


	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public CpaCheck size(Long size) {
		setSize(size);
		return this;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CpaCheck url(String url) {
		setUrl(url);
		return this;
	}
}
