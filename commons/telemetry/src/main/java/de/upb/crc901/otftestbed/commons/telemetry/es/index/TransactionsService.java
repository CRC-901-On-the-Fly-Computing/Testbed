package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Composition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Service;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.ServiceProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Transaction;

/**
 * This index stores the transactions of services.
 *
 * @author Roman
 *
 */
@Document(indexName = TransactionsService.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class TransactionsService extends Index<TransactionsService> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "transactions_service";


	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
	private Long time;

	@Field(type = FieldType.Object)
	private OtfProvider otfProvider;

	@Field(type = FieldType.Object)
	private ServiceProvider serviceProvider;

	@Field(type = FieldType.Object)
	private Composition composition;

	@Field(type = FieldType.Object)
	private Service service;

	@Field(type = FieldType.Object)
	private Transaction transaction;


	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public TransactionsService time(Long time) {
		setTime(time);
		return this;
	}


	public OtfProvider getOtfProvider() {
		return otfProvider;
	}

	public void setOtfProvider(OtfProvider otfProvider) {
		this.otfProvider = otfProvider;
	}

	public TransactionsService otfProvider(OtfProvider otfProvider) {
		setOtfProvider(otfProvider);
		return this;
	}


	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public TransactionsService serviceProvider(ServiceProvider serviceProvider) {
		setServiceProvider(serviceProvider);
		return this;
	}


	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public TransactionsService composition(Composition composition) {
		setComposition(composition);
		return this;
	}


	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public TransactionsService service(Service service) {
		setService(service);
		return this;
	}


	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TransactionsService transaction(Transaction transaction) {
		setTransaction(transaction);
		return this;
	}
}
