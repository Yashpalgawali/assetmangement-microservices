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

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.IEmployeeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("api")
@RestController
@RequiredArgsConstructor
public class EmployeeController {

	private final IEmployeeService employeeService;
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> createEmployee(@RequestBody EmployeeDto empDto) {
		
		employeeService.createEmployee(empDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Employee "+empDto.getEmployeeName()+" is created successfully", HttpStatus.CREATED));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		
		var list = employeeService.getAllEmployees();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
		
		var employee = employeeService.getEmployeeById(id);
		return ResponseEntity.status(HttpStatus.OK).body(employee );
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<EmployeeDto> getEmployeeByName(@PathVariable String name) {
		
		var employee = employeeService.getEmployeeByName(name);
		return ResponseEntity.status(HttpStatus.OK).body(employee );
	}
	
//	@GetMapping("/{id}")
//	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
//		
//		var employee = employeeService.getEmployeeById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(employee );
//	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateEmployee(@RequestBody EmployeeDto empDto) {
		
		employeeService.createEmployee(empDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Employee "+empDto.getEmployeeName()+" is created successfully", HttpStatus.CREATED));
	}
}
