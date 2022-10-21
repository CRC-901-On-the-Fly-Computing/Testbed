package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class AnalysisComposition {

	@Field(type = FieldType.Long)
	private Long numComp;

	@Field(type = FieldType.Long)
	private Long numError;

	@Field(type = FieldType.Float)
	private Float error;


	public Long getNumComp() {
		return numComp;
	}

	public void setNumComp(Long numComp) {
		this.numComp = numComp;
	}

	public AnalysisComposition numComp(Long numComp) {
		setNumComp(numComp);
		return this;
	}


	public Long getNumError() {
		return numError;
	}

	public void setNumError(Long numError) {
		this.numError = numError;
	}

	public AnalysisComposition numError(Long numError) {
		setNumError(numError);
		return this;
	}


	public Float getError() {
		return error;
	}

	public void setError(Float error) {
		this.error = error;
	}

	public AnalysisComposition error(Float error) {
		setError(error);
		return this;
	}

}
