package de.upb.crc901.otftestbed.commons.flow;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.upb.crc901.otftestbed.commons.flow.schema.Service;
import de.upb.crc901.otftestbed.commons.flow.schema.Template;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Template.class, name = "Template"),
    @JsonSubTypes.Type(value = Service.class, name = "Service")})
public class AbstractServiceMixin {

}
