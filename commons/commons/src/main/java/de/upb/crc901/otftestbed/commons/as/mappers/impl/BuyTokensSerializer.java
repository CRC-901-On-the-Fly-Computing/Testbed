package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crypto.math.serialization.converter.JSONConverter;

public class BuyTokensSerializer extends StdSerializer<BuyTokens>{


	/**
	 *
	 */
	private static final long serialVersionUID = 7910441857157851146L;

	public BuyTokensSerializer() {
		super(BuyTokens.class);
	}
	@Override
	public void serialize(BuyTokens value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		JSONConverter converter = new JSONConverter();
    gen.writeRawValue(converter.serialize(value.getRepresentation()));
	}
}
