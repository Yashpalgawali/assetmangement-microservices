package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;

public class EmployeeMapper {

	public static Employee mapToEmployee(EmployeeDto employeeDto, Employee emp) {

		emp.setEmpId(employeeDto.getEmpId());
		emp.setEmpName(employeeDto.getEmpName());
		emp.setDepartment(employeeDto.getDepartment());
		emp.setCompany(employeeDto.getCompany());

		return emp;
	}

	public static EmployeeDto mapToEmployeeDto(Employee emp, EmployeeDto employeeDto) {

		employeeDto.setEmpId(emp.getEmpId());
		employeeDto.setEmpName(emp.getEmpName());
		employeeDto.setDepartment(emp.getDepartment());
		employeeDto.setCompany(emp.getCompany());

		return employeeDto;
	}
}
