package de.upb.crc901.otftestbed.commons.repsys.mappers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.upb.crypto.math.serialization.Representable;
import de.upb.crypto.math.serialization.RepresentableRepresentation;
import de.upb.crypto.math.serialization.StandaloneRepresentable;
import de.upb.crypto.math.serialization.converter.JSONConverter;

public class RepresentableSerializer extends StdSerializer<Representable>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7910441857157851146L;
	
	public RepresentableSerializer() {
		super(Representable.class);
	}
	@Override
	public void serialize(Representable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		JSONConverter converter = new JSONConverter();
		//bug the key is indeed standalone representable, hence it will be handeled as such
		if (value instanceof StandaloneRepresentable) {
			gen.writeRawValue(converter.serialize(new RepresentableRepresentation(value)));
		}
		else {
			//this should never happen and will be removed with AP (Authentication-System)
			gen.writeRawValue(converter.serialize(value.getRepresentation()));
		}
	}
}


