package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//			ResponseEntity<DepartmentDto> departmentDto = deptClient.getDepartmentDto(null,empDto.getDepartmentId());

//			ResponseEntity<DesignationDto> designationDto = desigClient.getDesignationById(empDto.getDesignationId());

			Employee savedEmployee = emprepo.save(mappedEmployee);

			if (savedEmployee != null) {
				for (Long id : empDto.getAsset_ids()) {
					AssignAssets assignAssets = new AssignAssets();
					assignAssets.setAssetId(id);
					assignAssets.setEmployee(savedEmployee);

					AssignAssets assignedAssets = assignassetrepo.save(assignAssets);

					if (assignedAssets != null) {
						assetClient.updateAssetQuantity(id);
						ResponseEntity<AssetDto> assetById = assetClient.getAssetById(id);
						AssetDto assetByClient = assetById.getBody() != null ? assetById.getBody() : null;

						AssignAssetHistory assignHistory = new AssignAssetHistory();
						assignHistory.setAssetId(id);
						assignHistory.setEmpId(savedEmployee.getEmployeeId());
						assignHistory.setEmpName(savedEmployee.getEmployeeName());
						assignHistory.setAssetName(assetByClient.getAssetName());
						assignHistory.setAssettype(assetByClient.getAssetType().getAssetType());
						assignHistory.setAssignedDate(dateFormatter.format(LocalDateTime.now()));
						assignHistory.setAssignedTime(timeFormatter.format(LocalDateTime.now()));

						AssignAssetHistory save = assignassethistrepo.save(assignHistory);

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
	public EmployeeDto getEmployeeById(String correlationId, Long empId) {
		Optional<Employee> foundEmp = emprepo.findById(empId);
		if (foundEmp.isPresent()) {
			System.err.println("Found Employee " + foundEmp.get().toString());
			EmployeeDto mappedEmp = EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());

			DepartmentDto departmentDto = deptClient.getDepartmentDto(correlationId, mappedEmp.getDepartmentId());

			if (departmentDto != null) {
				mappedEmp.setCompanyId(departmentDto.getCompanyId());
				mappedEmp.setCompanyName(departmentDto.getCompanyName());
				mappedEmp.setDepartmentName(departmentDto.getDepartmentName());
				mappedEmp.setDepartmentId(departmentDto.getDepartmentId());
			} else {
				mappedEmp.setCompanyId(null);
				mappedEmp.setCompanyName("");
				mappedEmp.setDepartmentName("");
				mappedEmp.setDepartmentId(null);
			}

			ResponseEntity<DesignationDto> designationById = desigClient
					.getDesignationById(mappedEmp.getDesignationId());

			DesignationDto designationBody = designationById.getBody();
			if (designationBody != null) {

				mappedEmp.setDesignationName(designationBody.getDesignationName());
			} else {
				mappedEmp.setDesignationName("");
			}

			return mappedEmp;
		}

		throw new ResourceNotFoundException("Employee", "ID", "" + empId);
	}

	@Override
	public EmployeeDto getEmployeeByName(String correlationId, String name) {
		Optional<Employee> foundEmp = emprepo.findByEmployeeName(name);
		if (foundEmp.isPresent()) {
			return EmployeeMapper.mapToEmployeeDto(foundEmp.get(), new EmployeeDto());
		}
		throw new ResourceNotFoundException("Employee", "ID", name);
	}

	@Override
	public List<EmployeeDto> getEmployeeByDepartment(String correlationId, Long deptId) {
		var empList = emprepo.findByDepartmentId(deptId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(correlationId, empList);
		}
		throw new ResourceNotModifiedException("Employee", "Department", "" + deptId);
	}

	@Override
	public List<EmployeeDto> getEmployeeByCompany(String correlationId, Long compId) {
		var empList = emprepo.findByCompanyId(compId);
		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(correlationId, empList);
		}
		throw new ResourceNotFoundException("Employee", "Company", "" + compId);
	}

	@Override
	public List<EmployeeDto> getAllEmployees(String correlationId) {
		var empList = emprepo.findAll();

		if (empList.size() > 0) {
			return getEmployeeListMappedToDTO(correlationId, empList);
		}
		throw new ResourceNotFoundException("Employee", "List", "");
	}

	private List<EmployeeDto> getEmployeeListMappedToDTO(String correlationId, List<Employee> empList) {
		return empList.stream().map((emp) -> {

			EmployeeDto dto = EmployeeMapper.mapToEmployeeDto(emp, new EmployeeDto());

			ResponseEntity<DesignationDto> designationById = desigClient.getDesignationById(emp.getDesignationId());

			if (designationById.getBody() != null) {
				dto.setDesignationName(designationById.getBody().getDesignationName());
			}

			DepartmentDto departmentDto = deptClient.getDepartmentDto(correlationId, emp.getDepartmentId());

			if (departmentDto != null) {
				dto.setDepartmentName(departmentDto.getDepartmentName());
				dto.setCompanyName(departmentDto.getCompanyName());
			}

			return dto;

		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void updateEmployee(String correlationId, EmployeeDto empDto) {
		this.getEmployeeById(correlationId, empDto.getEmployeeId());

		int res = emprepo.updateEmployee(empDto.getEmployeeId(), empDto.getEmployeeName(), empDto.getDepartmentId(),
				empDto.getCompanyId());

		if (res < 0) {
			throw new ResourceNotModifiedException("Employee", "ID", "" + empDto.getEmployeeId());
		}
	}

}
