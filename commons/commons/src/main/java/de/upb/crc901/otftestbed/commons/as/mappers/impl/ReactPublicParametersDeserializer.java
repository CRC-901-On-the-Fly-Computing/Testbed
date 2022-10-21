package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;


public class ReactPublicParametersDeserializer extends StdDeserializer<ReactPublicParameters>{

	/**
	 *
	 */
	private static final long serialVersionUID = 4216559441244072999L;


	public ReactPublicParametersDeserializer() {
		super(ReactPublicParameters.class);
	}


	@Override
	public ReactPublicParameters deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JSONConverter converter = new JSONConverter();
		String represetatedString = p.readValueAsTree().toString();
		Representation repr = converter.deserialize(represetatedString);
		return new ReactPublicParameters (repr);
	}


}
