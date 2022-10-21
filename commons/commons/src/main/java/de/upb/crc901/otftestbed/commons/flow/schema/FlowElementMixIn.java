package de.upb.crc901.otftestbed.commons.flow.schema;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = AbstractService.class, name = "AbstractService"),
        @JsonSubTypes.Type(value = Assign.class, name = "Assign"),
        @JsonSubTypes.Type(value = Branch.class, name = "Branch"),
        @JsonSubTypes.Type(value = Foreach.class, name = "Foreach"),
        @JsonSubTypes.Type(value = Parallel.class, name = "Parallel"),
        @JsonSubTypes.Type(value = Skip.class, name = "Skip"), @JsonSubTypes.Type(value = While.class, name = "While") })
public class FlowElementMixIn {

}
