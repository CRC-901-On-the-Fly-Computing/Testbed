package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.ComputeCenter;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Hardware;

/**
 * This index stores the compute-centers and their provided hardware.
 *
 * @author Roman
 *
 */
@Document(indexName = ComputeCenters.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class ComputeCenters extends Index<ComputeCenters> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "providers_compute";


	@Field(type = FieldType.Object)
	private ComputeCenter computeCenter;

	@Field(type = FieldType.Object)
	private Hardware hardware;


	public ComputeCenter getComputeCenter() {
		return computeCenter;
	}

	public void setComputeCenter(ComputeCenter computeCenter) {
		this.computeCenter = computeCenter;
	}

	public ComputeCenters computeCenter(ComputeCenter computeCenter) {
		setComputeCenter(computeCenter);
		return this;
	}


	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

	public ComputeCenters hardware(Hardware hardware) {
		setHardware(hardware);
		return this;
	}
}
