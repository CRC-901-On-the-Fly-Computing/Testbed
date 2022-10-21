package de.upb.crc901.otftestbed.commons.utils;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.upb.crc901.otftestbed.commons.MessageMixin;
import de.upb.crc901.otftestbed.commons.as.mappers.SimpleACSModule;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactSystemManagerPublicIdentityDeserializer;
import de.upb.crc901.otftestbed.commons.as.mappers.impl.ReactVerifierPublicIdentityDeserializer;
import de.upb.crc901.otftestbed.commons.flow.AbstractServiceMixin;
import de.upb.crc901.otftestbed.commons.flow.FlowElementMixIn;
import de.upb.crc901.otftestbed.commons.flow.schema.AbstractService;
import de.upb.crc901.otftestbed.commons.flow.schema.FlowElement;
import de.upb.crc901.otftestbed.commons.repsys.mappers.RepresentationDeserializer;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;

public class Helpers {

  private static ObjectMapper mapper;

  private static InjectableValues.Std injValues = new InjectableValues.Std();

  static {
    //09.01.2018, mirkoj: add reputationsystem marshalling
    SimpleModule module = new SimpleModule("JSONModule", new Version(2, 0, 0, null, null, null));
    module.addDeserializer(Representation.class, new RepresentationDeserializer());
    module.addDeserializer(ReactSystemManagerPublicIdentity.class, new ReactSystemManagerPublicIdentityDeserializer());
    module.addDeserializer(ReactVerifierPublicIdentity.class, new ReactVerifierPublicIdentityDeserializer());
    //end

    mapper = new ObjectMapper();
    mapper.registerModule(module);
    mapper.registerModule(SimpleACSModule.getModule());
    mapper.setInjectableValues(injValues);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.addMixIn(FlowElement.class, FlowElementMixIn.class);
    mapper.addMixIn(AbstractService.class, AbstractServiceMixin.class);
    mapper.addMixIn(Message.class, MessageMixin.class);
    mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
  }

  public static void setInjectableValue(String key, Object value) {
    injValues.addValue(key, value);
  }

  private Helpers() {
    // used to hide the implicit public constructor
  }

  public static ObjectMapper getMapper() {
    return mapper;
  }
  
  public static String getScanPackages() {
    return "io.swagger.resources,"
        + "de.upb.crc901.otftestbed.commons.reputation"
        + "de.upb.crc901.otftestbed.commons.flow.schema"
        + "de.upb.crc901.otftestbed.commons.service_specification.schema";
  }
}
