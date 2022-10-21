package de.upb.crc901.otftestbed.service_requester.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such otf provider offered for the specified request.")
public class NoSuchOtfProviderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}