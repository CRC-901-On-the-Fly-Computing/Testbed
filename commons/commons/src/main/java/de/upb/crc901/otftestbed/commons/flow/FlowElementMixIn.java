package de.upb.crc901.otftestbed.commons.flow;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.upb.crc901.otftestbed.commons.flow.schema.AbstractService;
import de.upb.crc901.otftestbed.commons.flow.schema.Assign;
import de.upb.crc901.otftestbed.commons.flow.schema.Branch;
import de.upb.crc901.otftestbed.commons.flow.schema.Foreach;
import de.upb.crc901.otftestbed.commons.flow.schema.Parallel;
import de.upb.crc901.otftestbed.commons.flow.schema.Skip;
import de.upb.crc901.otftestbed.commons.flow.schema.While;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = AbstractService.class, name = "AbstractService"),
    @JsonSubTypes.Type(value = Assign.class, name = "Assign"),
    @JsonSubTypes.Type(value = Branch.class, name = "Branch"),
    @JsonSubTypes.Type(value = Foreach.class, name = "Foreach"),
    @JsonSubTypes.Type(value = Parallel.class, name = "Parallel"),
    @JsonSubTypes.Type(value = Skip.class, name = "Skip"),
    @JsonSubTypes.Type(value = While.class, name = "While")})
public class FlowElementMixIn {

}
