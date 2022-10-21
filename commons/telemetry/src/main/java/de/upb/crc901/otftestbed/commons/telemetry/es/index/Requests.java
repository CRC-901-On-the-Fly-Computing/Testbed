package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Domain;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Request;

/**
 * This index stores the service-requests.
 *
 * @author Roman
 *
 */
@Document(indexName = Requests.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class Requests extends Index<Requests> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "requests";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private Request request;

	@Field(type = FieldType.Object)
	private Domain domain;

	@Field(type = FieldType.Object)
	private OtfProvider otfProvider;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Requests time(Long time) {
		setTime(time);
		return this;
	}


	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Requests request(Request request) {
		setRequest(request);
		return this;
	}


	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Requests domain(Domain domain) {
		setDomain(domain);
		return this;
	}


	public OtfProvider getOtfProvider() {
		return otfProvider;
	}

	public void setOtfProvider(OtfProvider otfProvider) {
		this.otfProvider = otfProvider;
	}

	public Requests otfProvider(OtfProvider otfProvider) {
		setOtfProvider(otfProvider);
		return this;
	}
}
