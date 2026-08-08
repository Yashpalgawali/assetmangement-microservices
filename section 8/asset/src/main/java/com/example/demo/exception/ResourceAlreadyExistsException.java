package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends RuntimeException{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6542015137584044080L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
