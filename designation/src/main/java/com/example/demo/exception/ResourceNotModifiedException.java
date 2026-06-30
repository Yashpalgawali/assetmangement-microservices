package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceNotModifiedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8174298767528436925L;

	public ResourceNotModifiedException(String message) {
		super(message);
	}
}
