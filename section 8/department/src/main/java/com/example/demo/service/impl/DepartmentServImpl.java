package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.Company;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.DepartmentMessageDto;
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

	private final Logger logger = LoggerFactory.getLogger(DepartmentServImpl.class);

	private final StreamBridge streamBridge;

	@Override
	public void createDepartment(DepartmentDto departmentDto) {

		String departmentName = departmentDto.getDepartmentName();

		if (departmentName == null || departmentName.isBlank()) {
			throw new GlobalException("Deaprtment Name can't be blank");
		}

//		if(departmentDto.getDepartmentName().equals(" ") || departmentDto.getDepartmentName() == null) {
//			throw new GlobalException("Deaprment Name can't be blank");
//		}

		departmentDto.setDepartmentName(departmentDto.getDepartmentName().trim());

		Optional<Department> found = deptrepo.findByDepartmentNameAndCompanyId(departmentDto.getDepartmentName(),
				departmentDto.getCompanyId());

		if (found.isPresent()) {
			throw new ResourceAlreadyExistsException(
					"Department " + departmentDto.getDepartmentName() + " is already present in the company");
		}

		Department mapped = DepartmentMapper.mapToDepartment(departmentDto, new Department());

		Department savedDept = deptrepo.save(mapped);
		if (savedDept == null) {
			throw new GlobalException("Department " + departmentDto.getDepartmentName() + " is not created");
		}
		sendCommunication(savedDept);
	}

	private void sendCommunication(Department dept) {
		var deptMessageDto = new DepartmentMessageDto(dept.getDepartmentId(), dept.getDepartmentName());
		logger.warn("Sending Communication request for the details: {}", deptMessageDto);

		var result = streamBridge.send("sendCommunication-out-0", deptMessageDto);

		logger.warn("Is the Communication request processed successfully: {}", result);
	}

	@Override
	public List<DepartmentDto> getAllDepartments(String correlationId) {
		var list = deptrepo.findAll();
		if (list.size() > 0) {
			List<Company> compList = companyClient.getAllCompaniesList(correlationId).getBody();

			logger.debug("assetmanagement-correlation-id in getlldepartments() {} and the company List is {} ",
					correlationId, compList);
			return list.stream().map((dept) -> {
				logger.warn(" found deptList {}", dept);

				DepartmentDto deptDto = new DepartmentDto();
				deptDto.setDepartmentId(dept.getDepartmentId());
				deptDto.setDepartmentName(dept.getDepartmentName());

				Predicate<? super Company> predicate = p -> dept.getCompanyId() == p.getCompanyId();
				Optional<Company> company = compList.stream().filter(predicate).findFirst();

				if (company.isPresent()) {
					deptDto.setCompanyId(company.get().getCompanyId());
					deptDto.setCompanyName(company.get().getCompanyName());
				} else {
					deptDto.setCompanyId(dept.getCompanyId());
					deptDto.setCompanyName("");
				}

				return deptDto;
			}).collect(Collectors.toList());
		}
		throw new ResourceNotFoundException("Department", "Name", "name");
	}

	@Override
	public DepartmentDto getDepartmentById(String correlationId, Long deptId) {

		Department found = deptrepo.findById(deptId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "ID", String.valueOf(deptId)));
		DepartmentDto mapToDepartmentDto = DepartmentMapper.mapToDepartmentDto(found, new DepartmentDto());
		ResponseEntity<Company> companyById = companyClient.getCompanyById(correlationId, found.getCompanyId());

		logger.info("Found company {} ", companyById);

		if (null != companyById) {
			Company comp = companyById.getBody();
			mapToDepartmentDto.setCompanyName(comp.getCompanyName());
		} else {
			mapToDepartmentDto.setCompanyName("");
		}
		return mapToDepartmentDto;
	}

	@Override
	public DepartmentDto getDepartmentByDeptName(String correlationId, String deptName) {
		Department department = deptrepo.findByDepartmentName(deptName)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "Name", deptName));
		DepartmentDto deptDto = DepartmentMapper.mapToDepartmentDto(department, new DepartmentDto());
		Company comp = companyClient.getCompanyById(correlationId, department.getCompanyId()).getBody();
		deptDto.setCompanyName(comp.getCompanyName());
		return deptDto;
	}

	@Override
	@Transactional
	public void updateDepartment(DepartmentDto department) {

		Optional<String> foundDeptName = Optional.ofNullable(department.getDepartmentName());

		if (!foundDeptName.isPresent()) {
			throw new GlobalException("Deaprtment Name can't be blank");
		}

		deptrepo.findById(department.getDepartmentId()).orElseThrow(
				() -> new ResourceNotFoundException("Department", "ID", "" + department.getDepartmentId()));

		int res = deptrepo.updateDepartment(department.getDepartmentId(), department.getDepartmentName(),
				department.getCompanyId());
		if (res < 0) {
			throw new ResourceNotModifiedException("Department", "name", department.getDepartmentName());
		}
	}

	@Override
	public boolean updateCommunication(Long departmentId) {
		boolean isUpdated = false;

		if (departmentId != null) {
			Department department = deptrepo.findById(departmentId)
					.orElseThrow(() -> new ResourceNotFoundException("Department", "ID", "" + departmentId));
			department.setCommunicationSw(true);
			deptrepo.save(department);
			isUpdated = true;
		}

		return isUpdated;
	}

}
