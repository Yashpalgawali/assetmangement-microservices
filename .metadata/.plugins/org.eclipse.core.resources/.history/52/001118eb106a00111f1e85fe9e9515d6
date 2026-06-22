package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6368046666051229675L;
	 
	public ResourceNotFoundException(String resource, String resourceName , String resourceId) {
		super(String.format("Resource %d is not found %d : %d", resource, resourceName, resourceId ));
	}
}
