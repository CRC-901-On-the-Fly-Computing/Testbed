package de.upb.crc901.otftestbed.credential_issuer.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The credential issuer was not set up yet.")
public class CredentialIssuerNotInitializedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
