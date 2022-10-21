package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.requester.PerformanceMetrics;

public class NlpRequest {

	@Field(type = FieldType.Long)
	private Long executionTime;

	@Field(type = FieldType.Long)
	private Long sentenceAvgLength;

	@Field(type = FieldType.Long)
	private Long sentenceCount;

	@Field(type = FieldType.Long)
	private Long stringLength;


	public NlpRequest() {}

	public NlpRequest(PerformanceMetrics metrics) {
		setExecutionTime(metrics.getExecutionTime());
		setSentenceAvgLength(metrics.getSentenceAvgLength());
		setSentenceCount(metrics.getSentenceCount());
		setStringLength(metrics.getStringLength());
	}


	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public NlpRequest executionTime(Long executionTime) {
		setExecutionTime(executionTime);
		return this;
	}


	public Long getSentenceAvgLength() {
		return sentenceAvgLength;
	}

	public void setSentenceAvgLength(Long sentenceAvgLength) {
		this.sentenceAvgLength = sentenceAvgLength;
	}

	public NlpRequest sentenceAvgLength(Long sentenceAvgLength) {
		setSentenceAvgLength(sentenceAvgLength);
		return this;
	}


	public Long getSentenceCount() {
		return sentenceCount;
	}

	public void setSentenceCount(Long sentenceCount) {
		this.sentenceCount = sentenceCount;
	}

	public NlpRequest sentenceCount(Long sentenceCount) {
		setSentenceCount(sentenceCount);
		return this;
	}


	public Long getStringLength() {
		return stringLength;
	}

	public void setStringLength(Long stringLength) {
		this.stringLength = stringLength;
	}

	public NlpRequest stringLength(Long stringLength) {
		setStringLength(stringLength);
		return this;
	}
}
