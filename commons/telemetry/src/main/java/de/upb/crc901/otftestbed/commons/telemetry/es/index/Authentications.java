package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.requester.SimplePolicy;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.AccessPolicy;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Authentication;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Reputation;

/**
 * This index stores the authentication when someone buys or reviews a service-composition.
 *
 * @author Roman
 *
 */
@Document(indexName = Authentications.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class Authentications extends Index<Authentications> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "authentications";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private Authentication authentication;

	@Field(type = FieldType.Object)
	private AccessPolicy accessPolicy;

	@Field(type = FieldType.Object)
	private Reputation reputation;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Authentications time(Long time) {
		setTime(time);
		return this;
	}


	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public Authentications authentication(Authentication authentication) {
		setAuthentication(authentication);
		return this;
	}


	public AccessPolicy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(AccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public Authentications accessPolicy(AccessPolicy accessPolicy) {
		setAccessPolicy(accessPolicy);
		return this;
	}

	public void setAccessPolicy(SimplePolicy policy) {
		this.accessPolicy = new AccessPolicy(policy);
	}

	public Authentications accessPolicy(SimplePolicy policy) {
		setAccessPolicy(policy);
		return this;
	}


	public Reputation getReputation() {
		return reputation;
	}

	public void setReputation(Reputation reputation) {
		this.reputation = reputation;
	}

	public Authentications reputation(Reputation reputation) {
		setReputation(reputation);
		return this;
	}
}
