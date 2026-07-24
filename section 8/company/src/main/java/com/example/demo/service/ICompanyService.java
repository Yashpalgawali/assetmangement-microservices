package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Company;

public interface ICompanyService {

	public void saveCompany(Company company);	
	public Company getCompanyById(Long id);
	public Company getCompanyByName(String name);
	public List<Company> getAllCompanies();
	public void updateCompany(Company company);
	public boolean updateCommunicationStatus(Long companyId);
}
