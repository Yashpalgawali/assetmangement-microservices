package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service("empserv")
@RequiredArgsConstructor
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;

	@Override
	public void createEmployee(EmployeeDto empDto) {
		// TODO Auto-generated method stub

		if (!empDto.getEmpName().equals("")) {

			empDto.setEmpName(empDto.getEmpName().trim());
			Optional<Employee> byEmpName = emprepo.findByEmpName(empDto.getEmpName());
			if (!byEmpName.isEmpty()) {
				throw new ResourceAlreadyExistsException(
						"Employee is already present with the given name " + empDto.getEmpName());
			}

		}

	}

	@Override
	public EmployeeDto getEmployeeById(Long empId) {
		Optional<Employee> foundEmp = emprepo.findById(empId);
		if (foundEmp.isPresent()) {

			return EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());
		}

		throw new ResourceNotFoundException("Employee", "ID", "" + empId);
	}

	@Override
	public EmployeeDto getEmployeeByName(String name) {
		Optional<Employee> foundEmp = emprepo.findByEmpName(name);
		if (foundEmp.isPresent()) {
			return EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());
		}

		throw new ResourceNotFoundException("Employee", "ID", name);
	}

	@Override
	public List<EmployeeDto> getEmployeeByDepartment(Long deptId) {
		var empList = emprepo.findByDepartment(deptId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotModifiedException("Employee", "Department", "" + deptId);
	}

	@Override
	public List<EmployeeDto> getEmployeeByCompany(Long compId) {
		var empList = emprepo.findByCompany(compId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotFoundException("Employee", "Company", "" + compId);
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		var empList = emprepo.findAll();
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotFoundException("Employee", "List", "");
	}

	private List<EmployeeDto> getEmployeeListMappedToDTO(List<Employee> empList) {
		List<EmployeeDto> collect = empList.stream().map((emp) -> {

			EmployeeDto dto = EmployeeMapper.mapToEmployeeDto(emp, new EmployeeDto());
			return dto;

		}).collect(Collectors.toList());
		return collect;
	}

	@Override
	public void updateEmployee(EmployeeDto empDto) {
		this.getEmployeeById(empDto.getEmpId());
		
		int res = emprepo.updateEmployee(empDto.getEmpId(), empDto.getEmpName(), empDto.getDepartment(), empDto.getCompany());
		
		if(res< 0) {
			throw new ResourceNotModifiedException("Employee", "ID", ""+empDto.getEmpId());
		}
	}

}
