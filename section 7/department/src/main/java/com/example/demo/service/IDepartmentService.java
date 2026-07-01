package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.DepartmentDto;

public interface IDepartmentService {

	public void createDepartment(DepartmentDto department);	
	public List<DepartmentDto> getAllDepartments();
	public DepartmentDto getDepartmentById(Long deptId);
	public DepartmentDto getDepartmentByDeptName(String deptName);
	public void updateDepartment(DepartmentDto department);

}
