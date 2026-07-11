package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

import com.example.demo.dto.DepartmentContactInfoDto;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.IDepartmentService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;

@RequestMapping("department")
@RestController
@RequiredArgsConstructor
public class DepartmentController {

	private final IDepartmentService deptserv;
	
	private final DepartmentContactInfoDto departmentContactInfoDto;
	
	private final Environment env;
	
	private final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
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
	public ResponseEntity<DepartmentDto> getDepartmentDto(@RequestHeader("assetmanagement-correlation-id") String correlationId ,@PathVariable Long id){
		DepartmentDto departmentDto = deptserv.getDepartmentById(correlationId,id);
		return ResponseEntity.status(HttpStatus.OK).body(departmentDto);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<DepartmentDto> getDepartmentDto(@RequestHeader("assetmanagement-correlation-id") String correlationId ,@PathVariable String name){
		DepartmentDto departmentDto = deptserv.getDepartmentByDeptName(correlationId,name);
		return ResponseEntity.status(HttpStatus.OK).body(departmentDto);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<DepartmentDto>> getAllDepartmentsDto(@RequestHeader("assetmanagement-correlation-id") String correlationId ){
		logger.debug("assetmanagement-correlation-id {} ",correlationId);
		List<DepartmentDto> deptList= deptserv.getAllDepartments(correlationId);
		return ResponseEntity.status(HttpStatus.OK).body(deptList);
	}

	@GetMapping("/contact-info")
	public ResponseEntity<DepartmentContactInfoDto> getBuildInfo(){
		 
		return ResponseEntity.status(HttpStatus.OK).body(departmentContactInfoDto);
	}
	

	@RateLimiter(name = "getJavaVersion",fallbackMethod = "getJavaVersionFallBack")
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion(){
		 
		return ResponseEntity.status(HttpStatus.OK).body(env.getProperty("JAVA_HOME"));
	}
	
	
	public ResponseEntity<String> getJavaVersionFallBack(Throwable throwable){
		 
		return ResponseEntity.status(HttpStatus.OK).body("Java 21");
	}
}
