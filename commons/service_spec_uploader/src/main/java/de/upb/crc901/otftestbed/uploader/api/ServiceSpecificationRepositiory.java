package de.upb.crc901.otftestbed.uploader.api;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import de.upb.crc901.otftestbed.commons.service_specification.schema.ServiceSpecification;



public interface ServiceSpecificationRepositiory extends MongoRepository<ServiceSpecification, ObjectId>{

	public ServiceSpecification findServiceSpecificationByServiceName(String serviceName);
	
	public void deleteServiceSpecificationByServiceName(String serviceName);
}
