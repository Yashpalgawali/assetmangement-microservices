package com.example.demo.service.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.DepartmentDto;

@Component
public class DepartmentFallBack implements DepartmentFeignClient {

	@Override
	public DepartmentDto getDepartmentDto(String correlationId, Long id) {		
		return new DepartmentDto(null, "", null, "");		
		
	}

	@Override
	public List<DepartmentDto> getAllDepartmentsDto(String correlationId) {		
		return null;
	}
}
