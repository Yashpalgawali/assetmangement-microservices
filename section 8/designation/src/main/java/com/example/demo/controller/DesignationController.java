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

import com.example.demo.dto.DesignationContactInfoDto;
import com.example.demo.dto.DesignationDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.IDesignationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("designation")
@RequiredArgsConstructor
public class DesignationController {

	private final IDesignationService desigServ;
	private final DesignationContactInfoDto desigContactInfoDto;
	@PostMapping("/")
	public ResponseEntity<ResponseDto> createDesignation(@RequestBody DesignationDto desigDto){
		
		desigServ.createDesignation(desigDto);		
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Designation "+desigDto.getDesignationName() +" is created successfully", HttpStatus.CREATED));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DesignationDto> getDesignationById(@PathVariable Long id ){
		
		DesignationDto desigDto = desigServ.getDesignationById(id);		
		return ResponseEntity.status(HttpStatus.OK).body(desigDto);
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<DesignationDto>> getAllDesignations(){
		
		var desigList = desigServ.getAllDesignations();
		return ResponseEntity.status(HttpStatus.OK).body(desigList);
	}
	
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateDesignation(@RequestBody DesignationDto desigDto){
		
		desigServ.updateDesignation(desigDto);		
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Designation "+desigDto.getDesignationName()+" is updated successfully" , HttpStatus.OK));
	}

	@GetMapping("/contact-info")
	public ResponseEntity<DesignationContactInfoDto> getContactInfo() {
		
		return ResponseEntity.status(HttpStatus.OK).body(desigContactInfoDto);
	}
}
