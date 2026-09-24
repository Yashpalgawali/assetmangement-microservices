package com.example.demo.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.DesignationDto;

@FeignClient(name = "designation", fallback = DepartmentFallBack.class)
public interface DesignationFeignClient {

	@GetMapping("/{id}")
	public ResponseEntity<DesignationDto> getDesignationById(@PathVariable Long id );
	
	@GetMapping("/")
	public ResponseEntity<List<DesignationDto>> getAllDesignations();
}
