package com.example.demo.controller;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import com.example.demo.dto.CompanyContactInfoDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Company;
import com.example.demo.service.ICompanyService;

import io.github.resilience4j.retry.annotation.Retry;
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
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class); 
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> createCompany(@RequestBody Company company )
	{
		compserv.saveCompany(company);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("company created successfully by Vaishnavi", HttpStatus.CREATED));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Company>> getAllCompaniesList(@RequestHeader("assetmanagement-correlation-id") String correlationId)
	{
		logger.debug("inside getall company list()  correlationID is  {}",correlationId);
		
		var list = compserv.getAllCompanies();
		logger.debug("inside getall company list() is {}",list);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompanyById(@RequestHeader("assetmanagement-correlation-id") String correlationId,  @PathVariable Long id)
	{
		logger.info(" inside  company controller assetmangement-correlationID found is {}",correlationId);
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
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Company Updated Successfully by Vaishnavi", HttpStatus.OK));
	}
	
	@GetMapping("/build-version")
	public ResponseEntity<String> getBuildVersion()
	{		
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallBack")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() throws TimeoutException
	{
		logger.info("getBuildInfo() is Invoked");
//		throw new NullPointerException();
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	
	public ResponseEntity<String> getBuildInfoFallBack(Throwable throwable) {
		logger.info("getBuildInfoFallBack() is Invoked");
		return ResponseEntity.status(HttpStatus.OK).body("0.9");
	}
	
	@GetMapping("/contact-info")
	public ResponseEntity<CompanyContactInfoDto> getContactInfo()
	{
		logger.info("Invoked contact-info of Company ");
		return ResponseEntity.status(HttpStatus.OK).body(companyContactInfoDto);
	}
	
}
