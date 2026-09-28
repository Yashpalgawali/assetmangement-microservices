package com.example.demo.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.dto.DepartmentDto;

@FeignClient(name = "department", fallback = DepartmentFallBack.class)
public interface DepartmentFeignClient {

//	@GetMapping(value = "/api/{id}", produces = "application/json")
//	public ResponseEntity<DepartmentDto> getDepartmentDto(@RequestHeader("assetmanagement-correlation-id") String correlationId,
//			@PathVariable Long id);
//
//	@GetMapping("/api/")
//	public ResponseEntity<List<DepartmentDto>> getAllDepartmentsDto(
//			@RequestHeader("assetmanagement-correlation-id") String correlationId);
	
	@GetMapping(value = "/api/{id}", produces = "application/json")
	public  DepartmentDto getDepartmentDto(@RequestHeader("assetmanagement-correlation-id") String correlationId,
			@PathVariable Long id);

	@GetMapping("/api/")
	public List<DepartmentDto> getAllDepartmentsDto(
			@RequestHeader("assetmanagement-correlation-id") String correlationId);
}
