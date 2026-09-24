package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;

public class EmployeeMapper {

	public static Employee mapToEmployee(EmployeeDto employeeDto, Employee emp) {

		emp.setEmployeeId(employeeDto.getEmployeeId());
		emp.setEmployeeName(employeeDto.getEmployeeName());
		emp.setEmployeeEmail(employeeDto.getEmployeeEmail());
		emp.setEmployeeContact(employeeDto.getEmployeeContact());
		emp.setDepartmentId(employeeDto.getDepartmentId());
		emp.setCompanyId(employeeDto.getCompanyId());
		emp.setDesignationId(employeeDto.getDesignationId());
		emp.setEmployeeCode(employeeDto.getEmployeeCode());
		
		return emp;
	}

	public static EmployeeDto mapToEmployeeDto(Employee emp, EmployeeDto employeeDto) {

		employeeDto.setEmployeeId(emp.getEmployeeId());
		employeeDto.setEmployeeName(emp.getEmployeeName());
		employeeDto.setDepartmentId(emp.getDepartmentId());
		employeeDto.setCompanyId(emp.getCompanyId());
		employeeDto.setDesignationId(emp.getDesignationId());
		employeeDto.setEmployeeCode(emp.getEmployeeCode());
		employeeDto.setEmployeeEmail(emp.getEmployeeEmail());
		employeeDto.setEmployeeContact(emp.getEmployeeContact());
		
		return employeeDto;
	}
}
