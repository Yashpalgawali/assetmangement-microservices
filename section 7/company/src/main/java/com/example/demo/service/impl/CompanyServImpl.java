package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CompanyMessageDto;
import com.example.demo.entity.Company;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@Service("compserv")
@RequiredArgsConstructor
public class CompanyServImpl implements ICompanyService {

	private final CompanyRepository comprepo;
	
	private final Logger logger = LoggerFactory.getLogger(CompanyServImpl.class);
	
	private final StreamBridge streamBridge;

	@Override
	public void saveCompany(Company company) {
		
		if(company.getCompanyName()==null || company.getCompanyName().equals("")) {
			throw new GlobalException("Given Company name is null");
		}
		
		company.setCompanyName(company.getCompanyName().trim());
		boolean present = comprepo.findByCompanyName(company.getCompanyName()).isPresent();
		if(present) {
			throw new ResourceAlreadyExistsException("Company with name "+company.getCompanyName()+" is already present");
		}
		
		Company savedCompany = comprepo.save(company);
		if(savedCompany == null) {
			throw new GlobalException("Company "+company.getCompanyName()+" is not saved");
		}
		sendCommunication(savedCompany);
	}

	private void sendCommunication(Company savedcompany) {
		var messageDto = new CompanyMessageDto(savedcompany.getCompanyId(), savedcompany.getCompanyName());
		logger.info("Sending Communications request for the details {}" , messageDto);
		
		var result = streamBridge.send("sendCommunication-out-0", savedcompany);
		logger.info("Is the Communication Sending request processed successfully? :{}" , result);
	}
	
	@Override
	public Company getCompanyById(Long id) {
		 
		return comprepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "id", ""+id));
	}

	@Override
	public Company getCompanyByName(String name) {
		return comprepo.findByCompanyName(name).orElseThrow(() -> new ResourceNotFoundException("Company", "name", name));
		
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> companyList = comprepo.findAll();
		if(companyList.size() > 0)
			return companyList;
		throw new GlobalException("No Companies found");
	}

	@Override
	@Transactional
	public void updateCompany(Company company) {

		if(company.getCompanyName()==null) {
			throw new GlobalException("Given Company name is null");
		}
		company.setCompanyName(company.getCompanyName().trim());
		int result = comprepo.updateCompany(company.getCompanyId(), company.getCompanyName());
		if(result < 0) {
			throw new ResourceNotModifiedException("Company", "name", company.getCompanyName());
		}
	}

	@Override
	public boolean updateCommunicationStatus(Long companyId) {
		boolean isUpdated = false;
		if(companyId!=null) {
			Company company = comprepo.findById(companyId).orElseThrow(()-> new ResourceNotFoundException("Company", "ID", ""+companyId));
			
			company.setCommunicationSw(true);
			comprepo.save(company);
			isUpdated= true;
		}		
		return isUpdated;
	}

}
