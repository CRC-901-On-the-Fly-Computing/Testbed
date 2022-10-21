package de.upb.crc901.otftestbed.review_board.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="The service id given in the path does not match the service id of the issued token.")
public class InvalidReviewException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
