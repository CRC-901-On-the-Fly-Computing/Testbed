package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

public class ReactVerifierPublicIdentitySerializer extends StdSerializer<ReactVerifierPublicIdentity>{


	/**
	 *
	 */
	private static final long serialVersionUID = 7910441857157851146L;

	public ReactVerifierPublicIdentitySerializer() {
		super(ReactVerifierPublicIdentity.class);
	}
	@Override
	public void serialize(ReactVerifierPublicIdentity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		JSONConverter converter = new JSONConverter();
    gen.writeRawValue(converter.serialize(value.getRepresentation()));
	}
}
