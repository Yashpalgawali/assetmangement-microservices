package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.IDepartmentService;

import lombok.RequiredArgsConstructor;

@Service("deptserv")
@RequiredArgsConstructor
public class DepartmentServImpl implements IDepartmentService {

	private final DepartmentRepository deptrepo;
	
	@Override
	public void createDepartment(DepartmentDto department) {
		
		if(department.getDepartmentName().equals(" ") || department.getDepartmentName() == null) {
			throw new GlobalException("Deaprment Name can't be blank");
		}
				
		department.setDepartmentName(department.getDepartmentName().trim());
		
		Optional<Department> found =deptrepo.findByDepartmentName(department.getDepartmentName());
		
		if(found.isPresent()){
			throw new ResourceAlreadyExistsException("Department "+department.getDepartmentName()+"is already present");
		}
		
		Department mapped = DepartmentMapper.mapToDepartment(department, new Department());
		
		Department savedDept = deptrepo.save(mapped);
		if(savedDept!= null) {
			throw new GlobalException("Department "+department.getDepartmentName()+" is not created");
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
					deptDto.setCompanyId(dept.getCompanyId());
				return deptDto;
			}).collect(Collectors.toList());			
		}
			
		throw new GlobalException("No Department(s) found");
	}

	@Override
	public DepartmentDto getDepartmentById(Long deptId) {
		
		return null;
	}

	@Override
	public DepartmentDto getDepartmentByDeptName(String deptName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDepartment(DepartmentDto department) {
		// TODO Auto-generated method stub

	}

}
