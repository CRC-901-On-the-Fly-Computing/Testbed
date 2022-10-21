package de.upb.crc901.otftestbed.commons.reputation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crypto.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.interfaces.abe.StringAttribute;
import de.upb.crypto.react.acs.issuer.credentials.Attributes;
import de.upb.crypto.react.buildingblocks.attributes.AttributeNameValuePair;
import io.swagger.annotations.ApiModelProperty;

public class SimpleAttributes {

	@JsonProperty
	@ApiModelProperty(example="{\n" + 
			"    \"country\" : \"USA\",\n" + 
			"    \"subscriptionLevel\" : \"Prime\",\n" + 
			"    \"userLicense\" : \"Private\",\n" + 
			"    \"age\" : 20\n" + 
			"  }\n" +
			"")
	private Map<String, Object> attributePairs;

	public Map<String, Object> getAttributePairs() {
		return attributePairs;
	}

	public void setAttributePairs(Map<String, Object> attributePairs) {
		this.attributePairs = attributePairs;
	}

	@JsonIgnore
	public Attributes getAttributes() {
		List<AttributeNameValuePair> toReturn = new ArrayList<>();
		for (Map.Entry<String, Object> entry : attributePairs.entrySet()) {
			if (entry.getValue() instanceof Integer) {
				AttributeNameValuePair anvp = new AttributeNameValuePair(entry.getKey(),
						new BigIntegerAttribute((Integer) entry.getValue()));
				toReturn.add(anvp);
			}
			if (entry.getValue() instanceof String) {
				AttributeNameValuePair anvp = new AttributeNameValuePair(entry.getKey(),
						new StringAttribute((String) entry.getValue()));
				toReturn.add(anvp);
			}
		}
		AttributeNameValuePair[] pairsAsArray = new AttributeNameValuePair[toReturn.size()]; 
		return new Attributes(toReturn.toArray(pairsAsArray));
	}
}
