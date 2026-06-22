package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceNotModifiedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6368046666051229675L;
	 
	public ResourceNotModifiedException(String resource, String resourceName , String resourceId) {
		super(String.format("Resource %s is not found %s : %s", resource, resourceName, resourceId ));
	}
}
