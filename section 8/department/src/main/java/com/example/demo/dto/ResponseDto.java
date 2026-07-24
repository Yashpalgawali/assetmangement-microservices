package com.example.demo.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

	private String responseMessage;
	
	private HttpStatus responseStatus;
	
}
