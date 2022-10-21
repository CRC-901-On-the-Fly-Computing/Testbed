package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * This index stores the data points of the ampehre tool.
 *
 * @author Roman
 *
 */
@Document(indexName = Ampehre.INDEX_NAME, type = "log")
public class Ampehre extends Index<Ampehre> {

	public static final String INDEX_NAME = "ampehre";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_second")
	public Long timestamp_s;

	@Field(type = FieldType.Long)
	public Long timestamp_ns;

	@Field(type = FieldType.Float)
	public Float cpu0_power;

	@Field(type = FieldType.Float)
	public Float cpu1_power;

	@Field(type = FieldType.Float)
	public Float gpu_power;

	@Field(type = FieldType.Float)
	public Float fpga_power;

	@Field(type = FieldType.Float)
	public Float mic_power;

	@Field(type = FieldType.Float)
	public Float sys_power;

	@Field(type = FieldType.Integer)
	public Integer cpu0_temp;

	@Field(type = FieldType.Integer)
	public Integer cpu1_temp;

	@Field(type = FieldType.Integer)
	public Integer gpu_temp;

	@Field(type = FieldType.Float)
	public Float fpga_tempM;

	@Field(type = FieldType.Float)
	public Float fpga_tempI;

	@Field(type = FieldType.Integer)
	public Integer mic_temp;

	@Field(type = FieldType.Float)
	public Float sys_temp;

	@Field(type = FieldType.Float)
	public Float cpu0_clock;

	@Field(type = FieldType.Float)
	public Float cpu1_clock;

	@Field(type = FieldType.Integer)
	public Integer gpu_clock_graphics;

	@Field(type = FieldType.Integer)
	public Integer gpu_clock_mem;

	@Field(type = FieldType.Integer)
	public Integer mic_clock_core;

	@Field(type = FieldType.Integer)
	public Integer mic_clock_mem;

	@Field(type = FieldType.Float)
	public Float cpu_util;

	@Field(type = FieldType.Integer)
	public Integer gpu_util_core;

	@Field(type = FieldType.Integer)
	public Integer gpu_util_mem;

	@Field(type = FieldType.Float)
	public Float fpga_util;

	@Field(type = FieldType.Integer)
	public Integer mic_util;

	@Field(type = FieldType.Integer)
	public Integer cpu_memory;

	@Field(type = FieldType.Integer)
	public Integer cpu_swap;

	@Field(type = FieldType.Integer)
	public Integer gpu_memory;

	@Field(type = FieldType.Integer)
	public Integer mic_memory;

	@Field(type = FieldType.Integer)
	public Integer gpu_pcount;
}
