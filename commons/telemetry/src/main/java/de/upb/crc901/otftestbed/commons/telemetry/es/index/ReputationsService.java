package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Reputation;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Service;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.ServiceProvider;

/**
 * This index stores the reputations for the services.
 *
 * @author Roman
 *
 */
@Document(indexName = ReputationsService.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class ReputationsService extends Index<ReputationsService> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "reputations_service";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private ServiceProvider serviceProvider;

	@Field(type = FieldType.Object)
	private Service service;

	@Field(type = FieldType.Object)
	private Reputation reputation;


	public ReputationsService(ExtendedServiceReputation reputation) {
		setTime(reputation.getDate().getTime());
		setServiceProvider(
				new ServiceProvider()
				.id(reputation.getProviderUUID().toString())
				// TODO: add name to provider
				.name(null));
		setService(
				new Service()
				.name(reputation.getServiceCompositionId()));
		setReputation(
				new Reputation()
				.overall(reputation.getOverall())
				.usability(reputation.getUsability())
				.performance(reputation.getPerformance())
				.security(reputation.getSecurity())
				.other(reputation.getOther())
				.message(reputation.getReputationMessage()));
	}

	public ReputationsService(SearchableSignedReputation reputation) {
		setTime(reputation.getReputation().getDate().getTime());
		setServiceProvider(
				new ServiceProvider()
				.id(reputation.getOtfpUUID().toString())
				// TODO: add name to provider
				.name(null));
		setService(
				new de.upb.crc901.otftestbed.commons.telemetry.es.property.Service()
				.name(reputation.getItemName()));
		setReputation(
				new Reputation()
				.overall(reputation.getReputation().getOverall())
				.usability(reputation.getReputation().getUsability())
				.performance(reputation.getReputation().getPerformance())
				.security(reputation.getReputation().getSecurity())
				.other(reputation.getReputation().getOther())
				.message(reputation.getReputation().getReputationMessage()));
	}


	public static ReputationsService fromExtendedServiceReputation(ExtendedServiceReputation reputation) {
		return new ReputationsService(reputation);
	}

	public static ReputationsService fromSearchableSignedReputation(SearchableSignedReputation reputation) {
		return new ReputationsService(reputation);
	}


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public ReputationsService time(Long time) {
		setTime(time);
		return this;
	}


	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public ReputationsService serviceProvider(ServiceProvider serviceProvider) {
		setServiceProvider(serviceProvider);
		return this;
	}


	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ReputationsService service(Service service) {
		setService(service);
		return this;
	}


	public Reputation getReputation() {
		return reputation;
	}

	public void setReputation(Reputation reputation) {
		this.reputation = reputation;
	}

	public ReputationsService reputation(Reputation reputation) {
		setReputation(reputation);
		return this;
	}
}
