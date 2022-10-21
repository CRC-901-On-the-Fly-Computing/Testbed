package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.NlpRequest;

/**
 * This index stores the requests for natural language processing (B1).
 *
 * @author Roman
 *
 */
@Document(indexName = NlpRequests.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class NlpRequests extends Index<NlpRequests> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "nlp_requests";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Long)
	private Long duration;

	@Field(type = FieldType.Object)
	private NlpRequest nlpRequest;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public NlpRequests time(Long time) {
		setTime(time);
		return this;
	}


	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public NlpRequests duration(Long duration) {
		setDuration(duration);
		return this;
	}


	public NlpRequest getNlpRequest() {
		return nlpRequest;
	}

	public void setNlpRequest(NlpRequest nlpRequest) {
		this.nlpRequest = nlpRequest;
	}

	public NlpRequests nlpRequest(NlpRequest nlpRequest) {
		setNlpRequest(nlpRequest);
		return this;
	}
}
