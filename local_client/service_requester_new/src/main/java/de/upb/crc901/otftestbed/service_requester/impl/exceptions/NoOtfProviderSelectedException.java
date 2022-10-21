package de.upb.crc901.otftestbed.service_requester.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="There is no OTFProvider selected.")
public class NoOtfProviderSelectedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
