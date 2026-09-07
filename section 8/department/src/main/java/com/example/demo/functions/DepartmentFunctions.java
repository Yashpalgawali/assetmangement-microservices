package com.example.demo.functions;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.IDepartmentService;
import com.example.demo.service.impl.DepartmentServImpl;

@Configuration
public class DepartmentFunctions {
	
	private final Logger logger = LoggerFactory.getLogger(DepartmentServImpl.class);

	@Bean
	Consumer<Long> updateCommunication(IDepartmentService deptServ) {
		return departmentId -> {
			logger.info("Updating communication status for the Department ID "+departmentId.toString());
			deptServ.updateCommunication(departmentId);
		};
	}
}
