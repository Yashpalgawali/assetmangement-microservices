package com.example.demo.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.dto.Company;

@FeignClient(value = "company")
public interface CompanyFeignClient {

    @GetMapping(value = "/company/{id}", produces = "application/json")
    public ResponseEntity<Company> getCompanyById(@RequestHeader("assetmanagement-correlation-id") String correlationId,@PathVariable Long id);

    @GetMapping("/company/")
    public ResponseEntity<List<Company>> getAllCompaniesList(@RequestHeader("assetmanagement-correlation-id") String correlationId);
}

