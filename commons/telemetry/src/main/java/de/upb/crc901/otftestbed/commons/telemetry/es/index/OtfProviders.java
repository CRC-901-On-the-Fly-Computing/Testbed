package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Domain;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;

/**
 * This index stores the otf providers and their subscribed domains.
 *
 * @author Roman
 *
 */
@Document(indexName = OtfProviders.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class OtfProviders extends Index<OtfProviders> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "providers_otf";


	@Field(type = FieldType.Object)
	private OtfProvider otfProvider;

	@Field(type = FieldType.Object)
	private Domain domain;


	public OtfProvider getOtfProvider() {
		return otfProvider;
	}

	public void setOtfProvider(OtfProvider otfProvider) {
		this.otfProvider = otfProvider;
	}

	public OtfProviders otfProvider(OtfProvider otfProvider) {
		setOtfProvider(otfProvider);
		return this;
	}


	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public OtfProviders domain(Domain domain) {
		setDomain(domain);
		return this;
	}
}
