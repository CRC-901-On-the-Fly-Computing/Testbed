package de.upb.crc901.otftestbed.commons.requester;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceMetrics {

	@JsonProperty("execution_time")
	private Long executionTime;

	@JsonProperty("sentence_avg_length")
	private Long sentenceAvgLength;

	@JsonProperty("sentence_count")
	private Long sentenceCount;

	@JsonProperty("string_length")
	private Long stringLength;


	@JsonProperty("execution_time")
	public Long getExecutionTime() {
		return executionTime;
	}

	@JsonProperty("execution_time")
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}


	@JsonProperty("sentence_avg_length")
	public Long getSentenceAvgLength() {
		return sentenceAvgLength;
	}

	@JsonProperty("sentence_avg_length")
	public void setSentenceAvgLength(Long sentenceAvgLength) {
		this.sentenceAvgLength = sentenceAvgLength;
	}


	@JsonProperty("sentence_count")
	public Long getSentenceCount() {
		return sentenceCount;
	}

	@JsonProperty("sentence_count")
	public void setSentenceCount(Long sentenceCount) {
		this.sentenceCount = sentenceCount;
	}


	@JsonProperty("string_length")
	public Long getStringLength() {
		return stringLength;
	}

	@JsonProperty("string_length")
	public void setStringLength(Long stringLength) {
		this.stringLength = stringLength;
	}
}
