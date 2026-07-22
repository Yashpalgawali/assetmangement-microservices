package com.example.demo.functions;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.ICompanyService;

@Configuration
public class CompanyFunctions {

	private static Logger logger = LoggerFactory.getLogger(CompanyFunctions.class);
	
	@Bean
	Consumer<Long> updateCommunication(ICompanyService companyService) {
		
		return companyId -> {
			logger.info("Updating Communications status for the Company ID "+companyId);
			companyService.updateCommunicationStatus(companyId);
		}; 
	}
}
