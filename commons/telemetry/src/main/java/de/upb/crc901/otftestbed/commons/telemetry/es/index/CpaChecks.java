package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.CpaCheck;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Service;

/**
 * This index stores the results of the cpa-checks.
 *
 * @author Roman
 *
 */
@Document(indexName = CpaChecks.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class CpaChecks extends Index<CpaChecks> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "cpa_checks";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_second")
	private Long time;

	@Field(type = FieldType.Long)
	private Long duration;

	@Field(type = FieldType.Object)
	private Service service;

	@Field(type = FieldType.Object)
	private CpaCheck cpaCheck;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public CpaChecks time(Long time) {
		setTime(time);
		return this;
	}


	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public CpaChecks duration(Long duration) {
		setDuration(duration);
		return this;
	}


	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public CpaChecks service(Service service) {
		setService(service);
		return this;
	}


	public CpaCheck getCpaCheck() {
		return cpaCheck;
	}

	public void setCpaCheck(CpaCheck cpaCheck) {
		this.cpaCheck = cpaCheck;
	}

	public CpaChecks cpaCheck(CpaCheck cpaCheck) {
		setCpaCheck(cpaCheck);
		return this;
	}
}
