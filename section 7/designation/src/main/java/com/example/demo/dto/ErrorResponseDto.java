package com.example.demo.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

	private String apiPath;
	private String errorMessage;
	private HttpStatus errorStatus;
	private LocalDateTime errorTime;
}
