package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AssetDto;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.DesignationDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.AssignAssetHistory;
import com.example.demo.entity.AssignAssets;
import com.example.demo.entity.Employee;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.AssignAssetHistoryRepository;
import com.example.demo.repository.AssignAssetRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.IAssignAssetsService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.client.AssetFeignClient;
import com.example.demo.service.client.DepartmentFeignClient;
import com.example.demo.service.client.DesignationFeignClient;

import lombok.RequiredArgsConstructor;

@Service("empserv")
@RequiredArgsConstructor
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DepartmentFeignClient deptClient;
	private final AssignAssetRepository assignassetrepo;
	private final AssignAssetHistoryRepository assignassethistrepo;

//	private final IAssignAssetsService assignassetserv;
	private final AssetFeignClient assetClient;
	private final DesignationFeignClient desigClient;

	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void createEmployee(EmployeeDto empDto) {

		empDto.setEmployeeId(null);
		System.err.println("The EMployee Object IS " + empDto.toString());

		if (empDto.getEmployeeName() != "") {

			empDto.setEmployeeName(empDto.getEmployeeName().trim());
			Optional<Employee> byEmpName = emprepo.findByEmployeeName(empDto.getEmployeeName());
			if (!byEmpName.isEmpty()) {
				throw new ResourceAlreadyExistsException(
						"Employee is already present with the given name " + empDto.getEmployeeName());
			}
			empDto.setEmployeeId(null);
			Employee mappedEmployee = EmployeeMapper.mapToEmployee(empDto, new Employee());
			mappedEmployee.setEmployeeId(null);
			System.err.println("The EMployee Object IS " + empDto.toString());

//			ResponseEntity<DepartmentDto> departmentDto = deptClient.getDepartmentDto(null,empDto.getDepartmentId());

//			ResponseEntity<DesignationDto> designationDto = desigClient.getDesignationById(empDto.getDesignationId());

			Employee savedEmployee = emprepo.save(mappedEmployee);
			System.err.println("Saved Employee " + savedEmployee);

			if (savedEmployee != null) {
				for (Long id : empDto.getAsset_ids()) {
					AssignAssets assignAssets = new AssignAssets();
					assignAssets.setAssetId(id);
					assignAssets.setEmpId(savedEmployee.getEmployeeId());

					AssignAssets assignedAssets = assignassetrepo.save(assignAssets);
//					System.err.println("Assigned Asset " + assignedAssets.toString());

					if (assignedAssets != null) {
						ResponseEntity<AssetDto> assetById = assetClient.getAssetById(id);
						AssetDto assetByClient = assetById.getBody() != null ? assetById.getBody() : null;

						AssignAssetHistory assignHistory = new AssignAssetHistory();
						assignHistory.setAssetId(id);
						assignHistory.setEmpName(savedEmployee.getEmployeeName());
						assignHistory.setAssetName(assetByClient.getAssetName());
						assignHistory.setAssettype(assetByClient.getAssetTypeDto().getAssetType());
						assignHistory.setAssignedDate(dateFormatter.format(LocalDateTime.now()));
						assignHistory.setAssignedTime(timeFormatter.format(LocalDateTime.now()));

						AssignAssetHistory save = assignassethistrepo.save(assignHistory);

						System.err.println("Saved Assigned Asset History "+save.toString());	
					} else {
						throw new GlobalException("The asset(s) are not assigned");
					}
				}
			} else {
				throw new GlobalException("Employee " + empDto.getEmployeeName() + " is not saved");
			}

		}

	}

	@Override
	public EmployeeDto getEmployeeById(Long empId) {
		Optional<Employee> foundEmp = emprepo.findById(empId);
		if (foundEmp.isPresent()) {
			EmployeeDto mappedEmp = EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());

			ResponseEntity<DepartmentDto> departmentDto = deptClient.getDepartmentDto(null,
					mappedEmp.getDepartmentId());

			if (departmentDto != null) {
				DepartmentDto deptBody = departmentDto.getBody();
				mappedEmp.setCompanyName(deptBody.getCompanyName());
				mappedEmp.setDepartmentName(deptBody.getDepartmentName());
				mappedEmp.setDepartmentId(deptBody.getDepartmentId());
			} else {
				mappedEmp.setCompanyName("");
				mappedEmp.setDepartmentName("");
				mappedEmp.setDepartmentId(null);
			}

			ResponseEntity<DesignationDto> designationById = desigClient
					.getDesignationById(mappedEmp.getDesignationId());
			if (designationById != null) {
				DesignationDto designationBody = designationById.getBody();
				mappedEmp.setDesignationName(designationBody.getDesignationName());
			} else {
				mappedEmp.setDesignationName("");
			}
			return null;
		}

		throw new ResourceNotFoundException("Employee", "ID", "" + empId);
	}

	@Override
	public EmployeeDto getEmployeeByName(String name) {
		Optional<Employee> foundEmp = emprepo.findByEmployeeName(name);
		if (foundEmp.isPresent()) {
			return EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());
		}
		throw new ResourceNotFoundException("Employee", "ID", name);
	}

	@Override
	public List<EmployeeDto> getEmployeeByDepartment(Long deptId) {
		var empList = emprepo.findByDepartmentId(deptId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotModifiedException("Employee", "Department", "" + deptId);
	}

	@Override
	public List<EmployeeDto> getEmployeeByCompany(Long compId) {
		var empList = emprepo.findByCompanyId(compId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotFoundException("Employee", "Company", "" + compId);
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		var empList = emprepo.findAll();
		empList.forEach(System.out::print);

		System.err.println("Employee List " + empList.toString());
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(empList);
		}
		throw new ResourceNotFoundException("Employee", "List", "");
	}

	private List<EmployeeDto> getEmployeeListMappedToDTO(List<Employee> empList) {
		return  empList.stream().map((emp) -> {

			EmployeeDto dto = EmployeeMapper.mapToEmployeeDto(emp, new EmployeeDto());

			ResponseEntity<DesignationDto> designationById = desigClient.getDesignationById(emp.getDesignationId());

			if (designationById.getBody() != null) {
				dto.setDesignationName(designationById.getBody().getDesignationName());
			}

			ResponseEntity<DepartmentDto> departmentDto = deptClient.getDepartmentDto(null, emp.getDepartmentId());
			
			if(departmentDto.getBody()!=null) {
				dto.setDepartmentName(departmentDto.getBody().getDepartmentName());
				dto.setCompanyName(departmentDto.getBody().getCompanyName());
			}
			
			return dto;

		}).collect(Collectors.toList());
	}

	@Override
	public void updateEmployee(EmployeeDto empDto) {
		this.getEmployeeById(empDto.getEmployeeId());

		int res = emprepo.updateEmployee(empDto.getEmployeeId(), empDto.getEmployeeName(), empDto.getDepartmentId(),
				empDto.getCompanyId());

		if (res < 0) {
			throw new ResourceNotModifiedException("Employee", "ID", "" + empDto.getEmployeeId());
		}
	}

}
