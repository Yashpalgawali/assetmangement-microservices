package com.example.demo.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.Company;

@FeignClient(value = "company")
public interface CompanyFeignClient {

	@GetMapping(value ="/company/{id}", consumes = "application/json")
	public ResponseEntity<Company> getCompanyById(@PathVariable Long id);
}
