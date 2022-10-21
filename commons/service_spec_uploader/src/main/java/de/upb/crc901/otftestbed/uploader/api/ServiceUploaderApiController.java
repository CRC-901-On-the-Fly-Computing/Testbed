package de.upb.crc901.otftestbed.uploader.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import de.upb.crc901.otftestbed.commons.service_specification.schema.Operation;
import de.upb.crc901.otftestbed.commons.service_specification.schema.ServiceSpecification;
import de.upb.crc901.otftestbed.uploader.exceptions.NoServiceSpecificationFoundException;
import de.upb.crc901.otftestbed.uploader.exceptions.ServiceSpecificationAlreadyExistsException;

/**
 * 
 * @author Mirko Juergens
 *
 */
@Controller
public class ServiceUploaderApiController implements ServiceUploaderApi {
	@Autowired
	ServiceSpecificationRepositiory repo;
	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(ServiceUploaderApiController.class);

	@Override
	public ResponseEntity<ServiceSpecification> getServiceSpec(@PathVariable String serviceName) {
		// query the document (without the _id)
		log.debug("GET request for " + serviceName);
		ServiceSpecification spec = repo.findServiceSpecificationByServiceName(serviceName);
		if (spec == null)
			throw new NoServiceSpecificationFoundException();
		return ResponseEntity.ok(spec);
	}

	@Override
	public ResponseEntity<String> deleteServiceSpec(@PathVariable String serviceName) {
		log.debug("DELETE request for " + serviceName);
		ServiceSpecification spec = repo.findServiceSpecificationByServiceName(serviceName);
		log.debug("Found spec: " + spec);
		if (spec == null)
			throw new NoServiceSpecificationFoundException();
		repo.deleteServiceSpecificationByServiceName(serviceName);
		return new ResponseEntity<>("Service Specification successfully deleted", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ServiceSpecification> updateServiceSpec(@Valid @RequestBody ServiceSpecification body) {
		log.debug("POST request (update) for " + body.getServiceName());
		// count the existing db instances
		ServiceSpecification spec = repo.findServiceSpecificationByServiceName(body.getServiceName());
		if (spec == null)
			throw new NoServiceSpecificationFoundException();
		log.debug("Merging the operations!");
		List<Operation> toAdd = new ArrayList<>();
		for (Operation opInBody : body.getOperations()) {
			boolean opIsInDb = spec.getOperations().stream()
					.anyMatch((dbOp) -> opInBody.getOperationName().equals(dbOp.getOperationName()));
			if (!opIsInDb) {
				log.debug("Found Operation " + opInBody.toString() + " that is not yet in the db.");
				toAdd.add(opInBody);
			}
		}
		// merge!
		toAdd.addAll(spec.getOperations());
		spec.setOperations(toAdd);
		repo.deleteServiceSpecificationByServiceName(spec.getServiceName());
		repo.insert(spec);
		return new ResponseEntity<>(spec, HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<String> uploadServiceSpec(@Valid @RequestBody ServiceSpecification body) {
		
		log.debug("PUT request for " + body.getServiceName());
		ServiceSpecification specInDb = repo.findServiceSpecificationByServiceName(body.getServiceName());
		if (specInDb != null) {
			log.debug("There is already an service with that id in the db. Returning an error-message.");
			throw new ServiceSpecificationAlreadyExistsException();
		}
		repo.insert(body);
		return new ResponseEntity<>("Service spec succesfully created", HttpStatus.CREATED);
	}
}
