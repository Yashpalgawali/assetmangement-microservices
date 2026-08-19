package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.EmployeeDto;

public interface IEmployeeService {

	public void createEmployee(EmployeeDto empDto);
	
	public EmployeeDto getEmployeeById(Long empId);
	
	public EmployeeDto getEmployeeByName(String name);
	
	public List<EmployeeDto> getEmployeeByDepartment(Long deptId);
	
	public List<EmployeeDto> getEmployeeByCompany(Long compId);
	
	public List<EmployeeDto> getAllEmployees();
	
	public void updateEmployee(EmployeeDto empDto);
	
	
}
