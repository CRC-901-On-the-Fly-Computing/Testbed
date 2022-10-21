package de.upb.crc901.otftestbed.uploader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "There already exists a Service Specification for the given service name")
public class ServiceSpecificationAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2041799859254658140L;

}
