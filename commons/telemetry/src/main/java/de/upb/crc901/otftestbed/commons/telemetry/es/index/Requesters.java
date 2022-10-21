package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Requester;

/**
 * This index stores the registered requesters.
 *
 * @author Roman
 *
 */
@Document(indexName = Requesters.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class Requesters extends Index<Requesters> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "requesters";


	@Field(type = FieldType.Object)
	private Requester requester;


	public Requester getRequester() {
		return requester;
	}

	public void setRequester(Requester requester) {
		this.requester = requester;
	}

	public Requesters requester(Requester requester) {
		setRequester(requester);
		return this;
	}
}
