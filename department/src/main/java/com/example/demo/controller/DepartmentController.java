package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DepartmentContactInfoDto;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.IDepartmentService;

import lombok.RequiredArgsConstructor;

@RequestMapping("department")
@RestController
@RequiredArgsConstructor
public class DepartmentController {

	private final IDepartmentService deptserv;
	
	private final DepartmentContactInfoDto departmentContactInfoDto;
	@PostMapping("/")
	public ResponseEntity<ResponseDto> createDepartment(@RequestBody DepartmentDto deptDto){
		deptserv.createDepartment(deptDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Department "+deptDto.getDepartmentName()+" is created successfully", HttpStatus.CREATED));
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateDepartment(@RequestBody DepartmentDto deptDto){
		deptserv.updateDepartment(deptDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Department "+deptDto.getDepartmentName()+" is updated successfully", HttpStatus.OK));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DepartmentDto> getDepartmentDto(@PathVariable Long id){
		DepartmentDto departmentDto = deptserv.getDepartmentById(id);
		return ResponseEntity.status(HttpStatus.OK).body(departmentDto);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<DepartmentDto>> getAllDepartmentsDto(){
		List<DepartmentDto> deptList= deptserv.getAllDepartments();
		return ResponseEntity.status(HttpStatus.OK).body(deptList);
	}
	
	@GetMapping("/contact-info")
	public ResponseEntity<DepartmentContactInfoDto> getBuildInfo(){
		 
		return ResponseEntity.status(HttpStatus.OK).body(departmentContactInfoDto);
	}
}
