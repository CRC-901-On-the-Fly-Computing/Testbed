package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * This index stores the sent messages between otf providers.
 *
 * @author schwicht
 *
 */
@Document(indexName = OtfProviderNetwork.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class OtfProviderNetwork extends Index<OtfProviderNetwork> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "otf_provider_network";

	//[1544195591874, "NEW_EDGE", "6", "MP", "-65536", "SUB_SUP"],

	@Field(type = FieldType.Keyword)
	private List<String> message;


	public OtfProviderNetwork message(List<String> message) {
		this.message = message;
		return this;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}
}
