package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.IAssignAssetsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assignedassets/api")
@RequiredArgsConstructor
public class AssignedAssetsController {

	private final IAssignAssetsService assignAssetServ;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Void> getAssignedAssetsbyEmpId(@PathVariable Long id) {
		
		return null;
	}
}
