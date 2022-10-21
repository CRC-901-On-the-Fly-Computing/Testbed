package de.upb.crc901.otftestbed.service_requester.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="The provided attributes are not issued by the registered Issuer.")
public class InvalidAttributeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
