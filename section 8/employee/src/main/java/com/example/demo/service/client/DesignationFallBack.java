package com.example.demo.service.client;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.dto.DesignationDto;

@Component
public class DesignationFallBack implements DesignationFeignClient {
	
	private static final Logger logger = LoggerFactory.getLogger(DesignationFallBack.class);
	
	@Override
	public ResponseEntity<DesignationDto> getDesignationById(Long id) {
		logger.error("DESIGNATION FALLBACK: getDesignationById("+id+")");

		return ResponseEntity.ok(new DesignationDto(null, ""));
	}

	@Override
	public ResponseEntity<List<DesignationDto>> getAllDesignations() {

		logger.error("DESIGNATION FALLBACK: getAllDesignations()");
		
		return ResponseEntity.ok(Collections.emptyList());
	}
	
	
//	 private static final Logger logger =
//	            LoggerFactory.getLogger(DesignationFallBack.class);
//	@Override
//	public ResponseEntity<DesignationDto> getDesignationById(@PathVariable Long id ){
//		
//		logger.error("DESIGNATION FALLBACK: getAllDesignations()");
//
//		 return ResponseEntity.ok(
//		            new DesignationDto(null, "")
//		        );
//	}
//
//	@Override
//	public ResponseEntity<List<DesignationDto>> getAllDesignations(){
//		
//		return ResponseEntity.ok(Collections.emptyList());
//	}

}
