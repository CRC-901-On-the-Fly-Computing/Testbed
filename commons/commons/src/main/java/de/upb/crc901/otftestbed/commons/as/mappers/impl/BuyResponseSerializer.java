package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.upb.crc901.otftestbed.commons.reputation.BuyResponse;
import de.upb.crypto.math.serialization.converter.JSONConverter;

public class BuyResponseSerializer extends StdSerializer<BuyResponse>{


	/**
	 *
	 */
	private static final long serialVersionUID = 7910441857157851146L;

	public BuyResponseSerializer() {
		super(BuyResponse.class);
	}
	@Override
	public void serialize(BuyResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		JSONConverter converter = new JSONConverter();
    gen.writeRaw(converter.serialize(value.getRepresentation()));
	}
}
