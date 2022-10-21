package de.upb.crc901.otftestbed.uploader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Service Specification found for the given service name")
public class NoServiceSpecificationFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103934072656475982L;

}
