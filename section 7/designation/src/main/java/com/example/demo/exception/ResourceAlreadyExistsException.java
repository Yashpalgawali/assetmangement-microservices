package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3120082458296350144L;

	public ResourceAlreadyExistsException(String resource, String resourceName,String resourceValue) {
		super(String.format("Resource %s already exists for %s : %s",resource, resourceName, resourceValue ));
	}
}
