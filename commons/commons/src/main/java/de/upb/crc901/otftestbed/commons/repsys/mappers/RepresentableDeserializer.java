package de.upb.crc901.otftestbed.commons.repsys.mappers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.upb.crypto.math.serialization.Representable;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;


public class RepresentableDeserializer extends StdDeserializer<Representable>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4216559441244072999L;


	public RepresentableDeserializer() {
		super(Representable.class);
	}


	@Override
	public Representable deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JSONConverter converter = new JSONConverter();
		String represetatedString = p.readValueAsTree().toString();
		Representation repr = converter.deserialize(represetatedString);
		return (Representable) repr.repr().recreateRepresentable();
	}

	
}
