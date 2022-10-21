package de.upb.crc901.otftestbed.service_requester.impl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ServiceRequesterErrorHandler{

	@ExceptionHandler(InvalidAttributeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Object processInvalidAttributeException(InvalidAttributeException e) {
		throw e;
	}
	
	
	@ExceptionHandler(NoUserRegisteredException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Object processNoUserRegisteredException(NoUserRegisteredException e) {
		throw e;
	}
	
	@ExceptionHandler(NoSuchRequestException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Object processNoSuchRequestException(NoSuchRequestException e) {
		throw e;
	}
	
	
	
}
