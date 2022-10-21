package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Reputation {

	@Field(type = FieldType.Double)
	private Double overall;

	@Field(type = FieldType.Double)
	private Double usability;

	@Field(type = FieldType.Double)
	private Double performance;

	@Field(type = FieldType.Double)
	private Double security;

	@Field(type = FieldType.Double)
	private Double other;

	@Field(type = FieldType.Text)
	private String message;


	public Double getOverall() {
		return overall;
	}

	public void setOverall(Double overall) {
		this.overall = overall;
	}

	public Reputation overall(Double overall) {
		setOverall(overall);
		return this;
	}


	public Double getUsability() {
		return usability;
	}

	public void setUsability(Double usability) {
		this.usability = usability;
	}

	public Reputation usability(Double usability) {
		setUsability(usability);
		return this;
	}


	public Double getPerformance() {
		return performance;
	}

	public void setPerformance(Double performance) {
		this.performance = performance;
	}

	public Reputation performance(Double performance) {
		setPerformance(performance);
		return this;
	}


	public Double getSecurity() {
		return security;
	}

	public void setSecurity(Double security) {
		this.security = security;
	}

	public Reputation security(Double security) {
		setSecurity(security);
		return this;
	}


	public Double getOther() {
		return other;
	}

	public void setOther(Double other) {
		this.other = other;
	}

	public Reputation other(Double other) {
		setOther(other);
		return this;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Reputation message(String message) {
		setMessage(message);
		return this;
	}
}
