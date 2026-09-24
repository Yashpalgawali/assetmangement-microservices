package com.example.demo.service.client;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.dto.DepartmentDto;

@Component
public class DepartmentFallBack implements DepartmentFeignClient {

	@Override
	public ResponseEntity<DepartmentDto> getDepartmentDto(String correlationId, Long id) {
		
		DepartmentDto dept = new DepartmentDto(null, "", null, "");		
		return ResponseEntity.ok(dept);
	}

	@Override
	public ResponseEntity<List<DepartmentDto>> getAllDepartmentsDto(String correlationId) {
		
		return ResponseEntity.ok(Collections.emptyList());
	}

}
