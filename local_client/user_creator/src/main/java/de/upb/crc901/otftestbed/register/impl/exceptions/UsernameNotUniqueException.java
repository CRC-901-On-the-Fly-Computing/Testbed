package de.upb.crc901.otftestbed.register.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="The given username is already taken, please choose another one.")
public class UsernameNotUniqueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
