package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Service;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.ServiceProvider;

/**
 * This index stores the service providers and the provided services.
 *
 * @author Roman
 *
 */
@Document(indexName = ServiceProviders.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class ServiceProviders extends Index<ServiceProviders> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "providers_service";


	@Field(type = FieldType.Object)
	private ServiceProvider serviceProvider;

	@Field(type = FieldType.Object)
	private Service service;


	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public ServiceProviders serviceProvider(ServiceProvider serviceProvider) {
		setServiceProvider(serviceProvider);
		return this;
	}


	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceProviders service(Service service) {
		setService(service);
		return this;
	}
}
