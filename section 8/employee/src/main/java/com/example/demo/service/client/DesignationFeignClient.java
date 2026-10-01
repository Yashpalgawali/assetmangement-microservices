package com.example.demo.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.DesignationDto;

@FeignClient(name = "designation", fallback = DesignationFallBack.class)
public interface DesignationFeignClient {

	@GetMapping("/api/{id}")
	public ResponseEntity<DesignationDto> getDesignationById(@PathVariable Long id );
	
	@GetMapping("/api/")
	public ResponseEntity<List<DesignationDto>> getAllDesignations();
}
