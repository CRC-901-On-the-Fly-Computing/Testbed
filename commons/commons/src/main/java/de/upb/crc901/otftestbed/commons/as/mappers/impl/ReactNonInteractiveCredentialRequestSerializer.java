package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;

public class ReactNonInteractiveCredentialRequestSerializer extends StdSerializer<ReactNonInteractiveCredentialRequest>{


	/**
	 *
	 */
	private static final long serialVersionUID = 7910441857157851146L;

	public ReactNonInteractiveCredentialRequestSerializer() {
		super(ReactNonInteractiveCredentialRequest.class);
	}
	@Override
	public void serialize(ReactNonInteractiveCredentialRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		JSONConverter converter = new JSONConverter();
    gen.writeRawValue(converter.serialize(value.getRepresentation()));
	}
}
