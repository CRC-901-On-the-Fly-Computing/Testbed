package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Composition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Reputation;

/**
 * This index stores the reputations for the service-compositions.
 *
 * @author Roman
 *
 */
@Document(indexName = ReputationsComposition.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class ReputationsComposition extends Index<ReputationsComposition> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "reputations_composition";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private OtfProvider otfProvider;

	@Field(type = FieldType.Object)
	private Composition composition;

	@Field(type = FieldType.Object)
	private Reputation reputation;


	public ReputationsComposition(ExtendedServiceReputation reputation) {
		setTime(reputation.getDate().getTime());
		setOtfProvider(
				new OtfProvider()
				.id(reputation.getProviderUUID().toString())
				// TODO: add name to provider
				.name(null));
		setComposition(
				new Composition()
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

	public ReputationsComposition(SearchableSignedReputation reputation) {
		setTime(reputation.getReputation().getDate().getTime());
		setOtfProvider(
				new OtfProvider()
				.id(reputation.getOtfpUUID().toString())
				// TODO: add name to provider
				.name(null));
		setComposition(
				new Composition()
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


	public static ReputationsComposition fromExtendedServiceReputation(ExtendedServiceReputation reputation) {
		return new ReputationsComposition(reputation);
	}

	public static ReputationsComposition fromSearchableSignedReputation(SearchableSignedReputation reputation) {
		return new ReputationsComposition(reputation);
	}


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public ReputationsComposition time(Long time) {
		setTime(time);
		return this;
	}


	public OtfProvider getOtfProvider() {
		return otfProvider;
	}

	public void setOtfProvider(OtfProvider otfProvider) {
		this.otfProvider = otfProvider;
	}

	public ReputationsComposition otfProvider(OtfProvider otfProvider) {
		setOtfProvider(otfProvider);
		return this;
	}


	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public ReputationsComposition composition(Composition composition) {
		setComposition(composition);
		return this;
	}


	public Reputation getReputation() {
		return reputation;
	}

	public void setReputation(Reputation reputation) {
		this.reputation = reputation;
	}

	public ReputationsComposition reputation(Reputation reputation) {
		setReputation(reputation);
		return this;
	}
}
