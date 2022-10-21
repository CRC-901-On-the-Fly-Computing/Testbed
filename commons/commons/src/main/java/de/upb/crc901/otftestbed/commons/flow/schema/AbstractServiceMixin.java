package de.upb.crc901.otftestbed.commons.flow.schema;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Template.class, name = "Template"),
        @JsonSubTypes.Type(value = Service.class, name = "Service") })
public class AbstractServiceMixin {

}
