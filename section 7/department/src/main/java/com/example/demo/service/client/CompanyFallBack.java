package com.example.demo.service.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.dto.Company;

@Component
public class CompanyFallBack implements CompanyFeignClient {

	@Override
	public ResponseEntity<Company> getCompanyById(String correlationId, Long id) {
		
		  Company company = new Company();
	      company.setCompanyName("");
	      return ResponseEntity.ok(company);
	}

	@Override
	public ResponseEntity<List<Company>> getAllCompaniesList(String correlationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
