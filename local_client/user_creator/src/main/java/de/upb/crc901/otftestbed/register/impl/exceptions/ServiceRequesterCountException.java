package de.upb.crc901.otftestbed.register.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The number of Service-Requesters couldn't be fetched from Kubernetes.")
public class ServiceRequesterCountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
