package de.upb.crc901.otftestbed.review_board.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="The user reviewed this item already.")
public class DuplicateReviewException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
