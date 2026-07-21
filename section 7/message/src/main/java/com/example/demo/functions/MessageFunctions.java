package com.example.demo.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.CompanyMessageDto;

@Configuration
public class MessageFunctions {

	private static Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

//	For sending Email
	@Bean
	Function<CompanyMessageDto, CompanyMessageDto> email() {
		
		return companyMessageDto ->  {
			logger.info("Sending email with details {} ",companyMessageDto);
			return companyMessageDto;
		};
	}
	
//	For sending SMS
	@Bean
	Function<CompanyMessageDto, Long> sms() {
		
		return companyMessageDto ->  {
			logger.info("Sending SMS with details {} ",companyMessageDto);
			return companyMessageDto.companyId();
		};
	}
}
