package com.example.demo.service.client;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.DesignationDto;

@Component
public class DesignationFallBack implements DesignationFeignClient {

	@GetMapping("/{id}")
	public ResponseEntity<DesignationDto> getDesignationById(@PathVariable Long id ){
		
		DesignationDto dept = new DesignationDto(null, "");		
		return ResponseEntity.ok(dept);
	}

	@GetMapping("/")
	public ResponseEntity<List<DesignationDto>> getAllDesignations(){
		
		return ResponseEntity.ok(Collections.emptyList());
	}

}
