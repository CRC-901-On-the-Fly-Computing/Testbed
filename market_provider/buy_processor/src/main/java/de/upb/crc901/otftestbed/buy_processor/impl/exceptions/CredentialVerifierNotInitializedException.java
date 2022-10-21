package de.upb.crc901.otftestbed.buy_processor.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The credential verifier was not set up yet.")
public class CredentialVerifierNotInitializedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
