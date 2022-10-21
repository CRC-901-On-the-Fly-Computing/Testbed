package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.AnalysisComposition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Request;

/**
 * This index stores the service-composition analysis.
 *
 * @author Roman
 *
 */
@Document(indexName = Analysis.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class Analysis extends Index<Analysis> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "analysis";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Long)
	private Long duration;

	@Field(type = FieldType.Object)
	private Request request;

	@Field(type = FieldType.Object)
	private AnalysisComposition analysis;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Analysis time(Long time) {
		setTime(time);
		return this;
	}


	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Analysis duration(Long duration) {
		setDuration(duration);
		return this;
	}


	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Analysis request(Request request) {
		setRequest(request);
		return this;
	}


	public AnalysisComposition getAnalysis() {
		return analysis;
	}

	public void setAnalysis(AnalysisComposition analysis) {
		this.analysis = analysis;
	}

	public Analysis analysis(AnalysisComposition analysis) {
		setAnalysis(analysis);
		return this;
	}
}
