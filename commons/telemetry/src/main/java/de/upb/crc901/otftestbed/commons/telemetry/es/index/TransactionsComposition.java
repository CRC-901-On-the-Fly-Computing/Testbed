package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Composition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Requester;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Transaction;

/**
 * This index stores the transactions of service-compositions.
 *
 * @author Roman
 *
 */
@Document(indexName = TransactionsComposition.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class TransactionsComposition extends Index<TransactionsComposition> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "transactions_composition";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private Requester requester;

	@Field(type = FieldType.Object)
	private OtfProvider otfProvider;

	@Field(type = FieldType.Object)
	private Composition composition;

	@Field(type = FieldType.Object)
	private Transaction transaction;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public TransactionsComposition time(Long time) {
		setTime(time);
		return this;
	}


	public Requester getRequester() {
		return requester;
	}

	public void setRequester(Requester requester) {
		this.requester = requester;
	}

	public TransactionsComposition requester(Requester requester) {
		setRequester(requester);
		return this;
	}


	public OtfProvider getOtfProvider() {
		return otfProvider;
	}

	public void setOtfProvider(OtfProvider otfProvider) {
		this.otfProvider = otfProvider;
	}

	public TransactionsComposition otfProvider(OtfProvider otfProvider) {
		setOtfProvider(otfProvider);
		return this;
	}


	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public TransactionsComposition composition(Composition composition) {
		setComposition(composition);
		return this;
	}


	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TransactionsComposition transaction(Transaction transaction) {
		setTransaction(transaction);
		return this;
	}
}
