package de.upb.crc901.otftestbed.service_requester.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The user is not registered yet.")
public class NoUserRegisteredException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
