package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.EmployeeDto;

public interface IEmployeeService {

	public void createEmployee(EmployeeDto empDto);
	
	public EmployeeDto getEmployeeById(String correlationId,Long empId);
	
	public EmployeeDto getEmployeeByName(String correlationId, String name);
	
	public List<EmployeeDto> getEmployeeByDepartment(String correlationId,Long deptId);
	
	public List<EmployeeDto> getEmployeeByCompany(String correlationId,Long compId);
	
	public List<EmployeeDto> getAllEmployees(String correlationId);
	
	public void updateEmployee(String correlationId,EmployeeDto empDto);
	
}
