package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;

public class EmployeeMapper {

	public static Employee mapToEmployee(EmployeeDto employeeDto, Employee emp) {

		emp.setEmployeeId(employeeDto.getEmployeeId());
		emp.setEmployeeName(employeeDto.getEmployeeName());
		emp.setDepartmentId(employeeDto.getDepartmentId());
		emp.setCompanyId(employeeDto.getCompanyId());

		return emp;
	}

	public static EmployeeDto mapToEmployeeDto(Employee emp, EmployeeDto employeeDto) {

		employeeDto.setEmployeeId(emp.getEmployeeId());
		employeeDto.setEmployeeName(emp.getEmployeeName());
		employeeDto.setDepartmentId(emp.getDepartmentId());
		employeeDto.setCompanyId(emp.getCompanyId());

		return employeeDto;
	}
}
