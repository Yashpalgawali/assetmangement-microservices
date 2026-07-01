package com.example.demo.mapper;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;

public class DepartmentMapper {

	public static Department mapToDepartment(DepartmentDto deptDto, Department dept) {

		dept.setDepartmentName(deptDto.getDepartmentName());
		dept.setDepartmentId(deptDto.getDepartmentId());
		dept.setCompanyId(deptDto.getCompanyId());
		
		return dept;
	}

	public static DepartmentDto mapToDepartmentDto(Department dept, DepartmentDto deptDto) {

		deptDto.setDepartmentName(dept.getDepartmentName());
		deptDto.setDepartmentId(dept.getDepartmentId());
		deptDto.setCompanyId(dept.getCompanyId());
		deptDto.setCompanyName("");
		return deptDto;
	}
}
