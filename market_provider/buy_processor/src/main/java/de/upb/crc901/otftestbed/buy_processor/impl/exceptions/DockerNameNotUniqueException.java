package de.upb.crc901.otftestbed.buy_processor.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="The given username is already taken, please choose another one.")
public class DockerNameNotUniqueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
