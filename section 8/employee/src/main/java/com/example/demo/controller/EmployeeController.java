package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AssignAssetsDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.IAssignAssetsService;
import com.example.demo.service.IEmployeeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("api")
@RestController
@RequiredArgsConstructor
public class EmployeeController {

	private final IEmployeeService employeeService;
	
	private final IAssignAssetsService assignassetserv;

	@PostMapping("/")
	public ResponseEntity<ResponseDto> createEmployee(@RequestBody EmployeeDto empDto) {

		employeeService.createEmployee(empDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(
				"Employee " + empDto.getEmployeeName() + " is created successfully", HttpStatus.CREATED));
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {

		var list = employeeService.getAllEmployees();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@RequestHeader("assetmanagement-correlation-id") String correlationId, @PathVariable Long id) {

		var employee = employeeService.getEmployeeById(correlationId,id);
		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<EmployeeDto> getEmployeeByName(@PathVariable String name) {

		var employee = employeeService.getEmployeeByName(name);
		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}

//	@GetMapping("/{id}")
//	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
//		
//		var employee = employeeService.getEmployeeById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(employee );
//	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateEmployee(@RequestHeader("assetmanagement-correlation-id") String correlationId, @RequestBody EmployeeDto empDto) {

		employeeService.updateEmployee(correlationId, empDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(
				"Employee " + empDto.getEmployeeName() + " is created successfully", HttpStatus.OK));
	}
	
	@GetMapping("/getassignedassetsbyempid/{id}")
	public ResponseEntity<List<AssignAssetsDto>> getAssignedAssetsByEmpId(@RequestHeader("assetmanagement-correlation-id") String correlationId,@PathVariable Long id) {
		List<AssignAssetsDto> assignedAssetsList = assignassetserv.getAllAssignedAssetsByEmpId(correlationId,id);
		return ResponseEntity.status(HttpStatus.OK).body(assignedAssetsList);
	}
	@GetMapping("/viewassignedassets")
	public ResponseEntity<List<AssignAssetsDto>> getAlAssignedAssets() {
		List<AssignAssetsDto> assignedAssetsList = assignassetserv.getAllAssignedAssets();
		return ResponseEntity.status(HttpStatus.OK).body(assignedAssetsList);
	}
}
