package de.upb.crc901.otftestbed.commons.as.mappers.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

public class ReactVerifierPublicIdentityDeserializer extends StdDeserializer<ReactVerifierPublicIdentity> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4216559441244072999L;

	public static final String VALUE_NAME = ReactPublicParameters.class.toString();

	private BeanProperty prop = new BeanProperty.Std(new PropertyName(VALUE_NAME), null, null, null, null);


	public ReactVerifierPublicIdentityDeserializer() {
		super(ReactVerifierPublicIdentity.class);
	}


	@Override
	public ReactVerifierPublicIdentity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String representedString = p.readValueAsTree().toString();
		Representation repr = new JSONConverter().deserialize(representedString);
		Object pp = ctxt.findInjectableValue(VALUE_NAME, prop, null);
		if (!(pp instanceof ReactPublicParameters) && pp != null) {
			throw new IllegalArgumentException("Found injectable value with wrong type: " + pp.getClass().toString());
		}
		return new ReactVerifierPublicIdentity(repr, (ReactPublicParameters) pp);
	}
}
