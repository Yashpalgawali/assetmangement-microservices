package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.Company;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.IDepartmentService;
import com.example.demo.service.client.CompanyFeignClient;

import lombok.RequiredArgsConstructor;

@Service("deptserv")
@RequiredArgsConstructor
public class DepartmentServImpl implements IDepartmentService {

	private final DepartmentRepository deptrepo;
	
	private final CompanyFeignClient companyClient;
	
	@Override
	public void createDepartment(DepartmentDto departmentDto) {
		
		if(departmentDto.getDepartmentName().equals(" ") || departmentDto.getDepartmentName() == null) {
			throw new GlobalException("Deaprment Name can't be blank");
		}
				
		departmentDto.setDepartmentName(departmentDto.getDepartmentName().trim());
		
		Optional<Department> found =deptrepo.findByDepartmentNameAndCompanyId(departmentDto.getDepartmentName(),departmentDto.getCompanyId());
		
		if(found.isPresent()){
			throw new ResourceAlreadyExistsException("Department "+departmentDto.getDepartmentName()+"is already present in the company");
		}
		
		Department mapped = DepartmentMapper.mapToDepartment(departmentDto, new Department());
		
		Department savedDept = deptrepo.save(mapped);
		if(savedDept== null) {
			throw new GlobalException("Department "+departmentDto.getDepartmentName()+" is not created");
		}
		
	}

	@Override
	public List<DepartmentDto> getAllDepartments() {
		var list =deptrepo.findAll();
		if(list.size() >0 )
		{
			return list.stream().map((dept) -> {
				
				DepartmentDto deptDto = new DepartmentDto();
					
					deptDto.setDepartmentId(dept.getDepartmentId());
					deptDto.setDepartmentName(dept.getDepartmentName());
					Company company = companyClient.getCompanyById(dept.getCompanyId()).getBody();
					deptDto.setCompanyId(company.getCompanyId());
					deptDto.setCompanyName(company.getCompanyName());
					
				return deptDto;
			}).collect(Collectors.toList());			
		}
			
		throw new GlobalException("No Department(s) found");
	}

	@Override
	public DepartmentDto getDepartmentById(Long deptId) {
		
		Department found = deptrepo.findById(deptId).orElseThrow(()-> new ResourceNotFoundException("Department", "ID", String.valueOf(deptId)));
		DepartmentDto mapToDepartmentDto = DepartmentMapper.mapToDepartmentDto(found, new DepartmentDto());
		Company comp = companyClient.getCompanyById(found.getCompanyId()).getBody();
		mapToDepartmentDto.setCompanyName(comp.getCompanyName());
		return mapToDepartmentDto;
	}

	@Override
	public DepartmentDto getDepartmentByDeptName(String deptName) {
		Department department = deptrepo.findByDepartmentName(deptName).orElseThrow(()-> new ResourceNotFoundException("Department", "Name", deptName));
		DepartmentDto deptDto = DepartmentMapper.mapToDepartmentDto(department, new DepartmentDto());
		Company comp = companyClient.getCompanyById(department.getCompanyId()).getBody();
		deptDto.setCompanyName(comp.getCompanyName());
		return deptDto;
	}

	@Override
	@Transactional
	public void updateDepartment(DepartmentDto department) {
		
		deptrepo.findById(department.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department", "ID", ""+department.getDepartmentId()));
		
		int res = deptrepo.updateDepartment(department.getDepartmentId(), department.getDepartmentName(), department.getCompanyId());
		if(res < 0) {
			throw new ResourceNotModifiedException("Department", "name", department.getDepartmentName());
		}
	}

}
