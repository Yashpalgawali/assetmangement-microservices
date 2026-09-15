package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				LocalDateTime.now(), HttpStatus.NOT_FOUND);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}

	@ExceptionHandler(value = ResourceNotModifiedException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotModifiedException(ResourceNotModifiedException exception,
			WebRequest request) {
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				LocalDateTime.now(), HttpStatus.CONFLICT);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
	}

	@ExceptionHandler(value = GlobalException.class)
	public ResponseEntity<ErrorResponseDto> handleGlobalException(GlobalException exception, WebRequest request) {
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
	}

	@ExceptionHandler(value = ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(
			ResourceAlreadyExistsException exception, WebRequest request) {
		ErrorResponseDto errorDto = new ErrorResponseDto(request.getDescription(false), exception.getMessage(),
				LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> validationErrors = new HashMap<>();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

		validationErrorList.forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String validationMsg = error.getDefaultMessage();
			validationErrors.put(fieldName, validationMsg);

		});

		return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
	}
}
