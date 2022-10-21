package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.requester.SimplePolicy;

public class AccessPolicy {

	@Field(type = FieldType.Keyword)
	private List<String> allowedLicences;

	@Field(type = FieldType.Keyword)
	private List<String> allowedSubscriptionLevels;

	@Field(type = FieldType.Keyword)
	private List<String> allowedCountries;

	@Field(type = FieldType.Integer)
	private Integer minAge;

	@Field(type = FieldType.Integer)
	private Integer maxAge;


	public AccessPolicy() {}

	public AccessPolicy(SimplePolicy policy) {
		if (policy == null) {
			return;
		}

		allowedLicences = policy.getAllowedLicences();
		allowedSubscriptionLevels = policy.getAllowedSubscriptionLevels();
		allowedCountries = policy.getAllowedCountries();
		minAge = policy.getMinAge();
		maxAge = policy.getMaxAge();
	}


	public List<String> getAllowedLicences() {
		return allowedLicences;
	}

	public void setAllowedLicences(List<String> allowedLicences) {
		this.allowedLicences = allowedLicences;
	}

	public AccessPolicy allowedLicences(List<String> allowedLicences) {
		setAllowedLicences(allowedLicences);
		return this;
	}


	public List<String> getAllowedSubscriptionLevels() {
		return allowedSubscriptionLevels;
	}

	public void setAllowedSubscriptionLevels(List<String> allowedSubscriptionLevels) {
		this.allowedSubscriptionLevels = allowedSubscriptionLevels;
	}

	public AccessPolicy allowedSubscriptionLevels(List<String> allowedSubscriptionLevels) {
		setAllowedSubscriptionLevels(allowedSubscriptionLevels);
		return this;
	}


	public List<String> getAllowedCountries() {
		return allowedCountries;
	}

	public void setAllowedCountries(List<String> allowedCountries) {
		this.allowedCountries = allowedCountries;
	}

	public AccessPolicy allowedCountries(List<String> allowedCountries) {
		setAllowedCountries(allowedCountries);
		return this;
	}


	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public AccessPolicy minAge(Integer minAge) {
		setMinAge(minAge);
		return this;
	}


	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public AccessPolicy maxAge(Integer maxAge) {
		setMaxAge(maxAge);
		return this;
	}
}
