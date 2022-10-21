package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;


public class BuyTokensDeserializer extends StdDeserializer<BuyTokens>{

	/**
	 *
	 */
	private static final long serialVersionUID = 4216559441244072999L;


	public BuyTokensDeserializer() {
		super(BuyTokens.class);
	}


	@Override
	public BuyTokens deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JSONConverter converter = new JSONConverter();
		String represetatedString = p.readValueAsTree().toString();
		Representation repr = converter.deserialize(represetatedString);
		return new BuyTokens (repr);
	}


}
