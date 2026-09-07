package com.example.demo.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.DepartmentMessageDto;

@Configuration
public class MessageFunctions {
 
	private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);
	
	@Bean
	Function<DepartmentMessageDto, DepartmentMessageDto> email(){
		
		return departmentMessageDto -> {
			logger.info("Sending email with the details :- "+departmentMessageDto.toString());
			return departmentMessageDto;
		} ;
	}
	
	@Bean
	Function<DepartmentMessageDto, Long> sms(){
		
		return departmentMessageDto -> {
			logger.info("Sending SMS with the details :- "+departmentMessageDto.toString());
			return departmentMessageDto.departmentId();
		} ;
	}
	
	
}
