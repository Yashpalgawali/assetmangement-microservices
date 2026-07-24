package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

 
	/**
	 * 
	 */
	private static final long serialVersionUID = -377112223956862886L;

	public ResourceNotFoundException(String resource, String resourceName,String resourceValue) {
		super(String.format("Resource %s not found for %s : %s",resource, resourceName, resourceValue ));
	}
}
