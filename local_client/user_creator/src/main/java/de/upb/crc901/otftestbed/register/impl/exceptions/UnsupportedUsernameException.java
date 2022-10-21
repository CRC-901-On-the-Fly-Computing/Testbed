package de.upb.crc901.otftestbed.register.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The username is invalid please use only [a-z0-9]*.")
public class UnsupportedUsernameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
