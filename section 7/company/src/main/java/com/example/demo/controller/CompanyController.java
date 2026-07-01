package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CompanyContactInfoDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Company;
import com.example.demo.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("company")
@RequiredArgsConstructor
public class CompanyController {

	private final ICompanyService compserv;

	@Value("${build.version}")
	private String buildVersion;
	
	private final Environment env;
	
	private final CompanyContactInfoDto companyContactInfoDto;
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> createCompany(@RequestBody Company company )
	{
		compserv.saveCompany(company);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("company created successfully by Vaishnavi", HttpStatus.CREATED));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Company>> getAllCompaniesList()
	{
		var list = compserv.getAllCompanies();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompanyById(@PathVariable Long id)
	{
		var company = compserv.getCompanyById(id);
		return ResponseEntity.status(HttpStatus.OK).body(company);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Company> getCompanyByName(@PathVariable String name)
	{
		var company = compserv.getCompanyByName(name);
		return ResponseEntity.status(HttpStatus.OK).body(company);
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateCompany(@RequestBody Company company )
	{
		compserv.updateCompany(company);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("company Updated Successfully by Vaishnavi", HttpStatus.OK));
	}
	
	@GetMapping("/build-version")
	public ResponseEntity<String> getBuildVersion()
	{		
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo()
	{
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	@GetMapping("/contact-info")
	public ResponseEntity<CompanyContactInfoDto> getContactInfo()
	{
		return ResponseEntity.status(HttpStatus.OK).body(companyContactInfoDto);
	}
	
}
