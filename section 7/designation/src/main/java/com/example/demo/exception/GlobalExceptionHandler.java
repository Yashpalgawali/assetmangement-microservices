package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
		
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				                                         HttpStatus.NOT_FOUND, LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	
	@ExceptionHandler(value = ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request){
		
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				                                         HttpStatus.BAD_REQUEST, LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
	}
	
	@ExceptionHandler(value = ResourceNotModifiedException.class )
	public ResponseEntity<ErrorResponseDto> handleResourceNotModifiedException(ResourceNotModifiedException exception, WebRequest request){
		
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(), 
														HttpStatus.CONFLICT, LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
	}
	
	@ExceptionHandler(value = GlobalException.class )
	public ResponseEntity<ErrorResponseDto> handleGlobalException(GlobalException exception, WebRequest request){
				
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(), 
														 HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
	}
}
