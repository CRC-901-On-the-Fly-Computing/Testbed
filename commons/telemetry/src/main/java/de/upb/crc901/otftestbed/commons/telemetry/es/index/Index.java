package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is the base class for all indices.
 *
 * @author Roman
 *
 * @param <T> has to be the deriving class. It is used for the return type of the builder methods.
 */
public class Index<T extends Index<T>> {

	/**
	 * The default used type for the elasticsearch indices.
	 */
	public static final String DEFAULT_TYPE = "t";
	public static final String DEFAULT_INDEX_PREFIX = "mm_";

	@Id
	@JsonIgnore
	private String id;

	@Version
	@JsonIgnore
	private Long version;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public T id(String id) {
		setId(id);
		return (T)this;
	}


	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@SuppressWarnings("unchecked")
	public T version(Long version) {
		setVersion(version);
		return (T)this;
	}
}
