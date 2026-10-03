package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AssetDto;
import com.example.demo.dto.AssetType;
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
					assignAssets.setAssetAssignDate(dateFormatter.format(LocalDateTime.now()));
					assignAssets.setAssetAssignTime(timeFormatter.format(LocalDateTime.now()));
					
					System.err.println("AssignAset Object is "+assignAssets.toString());
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

						System.err.println("History object is "+assignHistory.toString());
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

		System.err.println("Inside updateEmployee() " + empDto.toString());
		String new_assets = "";
		List<Long> nl = empDto.getAsset_ids();

		// this.getEmployeeById(correlationId, empDto.getEmployeeId());

		int res = emprepo.updateEmployee(empDto.getEmployeeId(), empDto.getEmployeeName(), empDto.getDepartmentId(),
				empDto.getCompanyId(), empDto.getEmployeeEmail(), empDto.getEmployeeContact());

		if (nl != null) {
			if (nl.size() > 0) {
				for (int i = 0; i < nl.size(); i++) {
					if (i == 0) {
						new_assets = String.valueOf(nl.get(i));
					} else {
						new_assets = new_assets + "," + nl.get(i);
					}
				}
				AssignAssets isassigned = null;

				// Fetch managed Employee entity
				EmployeeDto managedEmp = this.getEmployeeById(correlationId, empDto.getEmployeeId());

				List<AssignAssets> assigned_assets = assignassetrepo
						.getAllAssignedAssetsByEmpId(empDto.getEmployeeId());

				String[] ol_assets = new String[assigned_assets.size()];
				String[] nw_assets = new String[new_assets.length()];

				nw_assets = new_assets.split(",");
				for (int i = 0; i < assigned_assets.size(); i++) {

					ol_assets[i] = assigned_assets.get(i).getAssetId().toString();
//			ol_assets[i] = assigned_assets.get(i).getAsset().getAsset_id().toString();
				}

				if (ol_assets.length == nw_assets.length) {
					List<String> olist = Arrays.asList(ol_assets);
					List<String> nlist = Stream.of(new_assets.split(",")).collect(Collectors.toList());

					for (int i = 0; i < ol_assets.length; i++) {
						if (nlist.contains(ol_assets[i])) {
							continue;
						} else {
							Long asid = Long.valueOf(ol_assets[i]);
							int output = assignassetrepo.deleteAssignedAssetByEmpidAssetId(asid,
									empDto.getEmployeeId());

							if (output > 0) {

								ResponseEntity<AssetDto> assetById = assetClient.getAssetById(asid);
								AssetDto assetDto = null;
								if (assetById != null) {
									assetDto = assetById.getBody();
								}

//						int qty = assetrepo.getQuantiyByAssetId(asid);
//						qty+=1;
//						
//						assetrepo.updateAssetQuantityByAssetId(asid, ""+qty);

//						AssetDto ast = new AssetDto();
//						
//						AssetDto getasset = assetrepo.findById(asid).get();
//						
//						AssetType atype = new AssetType();
//						
//						atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
//						
//						ast.setAtype(atype);
//						
//						ast.setAsset_id(asid);
//						ast.setAsset_name(getasset.getAsset_name());
//						ast.setAsset_number(getasset.getAsset_number());
//						ast.setModel_number(getasset.getModel_number());
//						ast.setQuantity(getasset.getQuantity());

								AssignAssetHistory ahist = new AssignAssetHistory();
								
								ahist.setAssetId(asid);
								ahist.setEmpId(empDto.getEmployeeId());
								ahist.setAssettype(assetDto.getAssetType().getAssetType());								
								ahist.setAssetName(assetDto.getAssetName());

								ahist.setEmpName(managedEmp.getEmployeeName());

								ahist.setUpdateDate(dateFormatter.format(LocalDateTime.now()));
								ahist.setUpdateTime(timeFormatter.format(LocalDateTime.now()));
								ahist.setAssettype(assetDto.getAssetType().getAssetType());

								assignassethistrepo.save(ahist);

							}
						}
					}

					for (int i = 0; i < nw_assets.length; i++) {
						if (olist.contains(nw_assets[i])) {
							continue;
						} else {
							AssignAssets assignasset = new AssignAssets();
							Long asid = Long.valueOf(nw_assets[i]);
							int qty = 0;

							ResponseEntity<AssetDto> assetById = assetClient.getAssetById(asid);
							AssetDto assetDto = null;
							if (assetById != null) {
								assetDto = assetById.getBody();
							}

							Long astid = Long.valueOf(asid);
							AssetDto ast = new AssetDto();

							// ast.setAtype(assetDto.getAssetType());

//					ast.setAsset_id(astid);
//					ast.setAsset_name(getasset.getAsset_name());
//					ast.setAsset_number(getasset.getAsset_number());
//					ast.setModel_number(getasset.getModel_number());
//					ast.setQuantity(getasset.getQuantity());

							assignasset.setEmployee(EmployeeMapper.mapToEmployee(managedEmp, new Employee()));
							assignasset.setAssetId(astid);

//					assignasset.setAssign_date(dateFormatter.format(LocalDateTime.now()));
//					assignasset.setAssign_time(dtime.format(LocalDateTime.now()));
//					
							isassigned = assignassetrepo.save(assignasset);

							if (isassigned != null) {
								qty = assetDto.getQty() - 1;

								assetClient.updateAssetQuantitybyAssetId(astid, (Integer) qty);

								AssignAssetHistory ahist = new AssignAssetHistory();

								ahist.setAssetId(asid);
								ahist.setEmpId(empDto.getEmployeeId());
								ahist.setAssettype(assetDto.getAssetType().getAssetType());

								ahist.setUpdateDate(dateFormatter.format(LocalDateTime.now()));
								ahist.setUpdateTime(timeFormatter.format(LocalDateTime.now()));
								ahist.setAssetName(assetDto.getAssetName());
								ahist.setEmpName(managedEmp.getEmployeeName());
//						ahist.setOperation_date(dateformatter.format(LocalDateTime.now()));
//						ahist.setOperation_time(dtime.format(LocalDateTime.now()));
//						ahist.setOperation("Asset Assigned");
								assignassethistrepo.save(ahist);

							}
						}
					}
				}

				// If AssetDto to be assigned are greater than the Already assigned assets
				if (nw_assets.length > ol_assets.length) {
					List<String> olist = Arrays.asList(ol_assets);
					List<String> nlist = Stream.of(new_assets.split(",")).collect(Collectors.toList());

					for (int i = 0; i < nw_assets.length; i++) {
						if (olist.contains(nw_assets[i])) {
							continue;
						} else {
							AssignAssets assignasset = new AssignAssets();
							Long asid = Long.valueOf(nw_assets[i]);
							int qty = 0;

							Long astid = Long.valueOf(asid);

							AssetDto assetDto = getAssetDtoByIdUsingAssetClient(asid);

//					AssetDto ast = new AssetDto();
//					
//					assetrepo.findById(astid);
//					AssetDto getasset = assetrepo.findById(astid).get();
//					
//					AssetType atype = new AssetType();
//					
//					atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
//					
//					ast.setAtype(atype);
//					
//					ast.setAsset_id(astid);
//					ast.setAsset_name(getasset.getAsset_name());
//					ast.setAsset_number(getasset.getAsset_number());
//					ast.setModel_number(getasset.getModel_number());
//					ast.setQuantity(getasset.getQuantity());

							assignasset.setEmployee(EmployeeMapper.mapToEmployee(managedEmp, new Employee()));
							assignasset.setAssetId(assetDto.getAssetId());

//					assignasset.setAssign_date(ddate.format(LocalDateTime.now()));
//					assignasset.setAssign_time(dtime.format(LocalDateTime.now()));

							isassigned = assignassetrepo.save(assignasset);

							if (isassigned != null) {

								qty = assetDto.getQty() - 1;

								assetClient.updateAssetQuantitybyAssetId(astid, (Integer) qty);

								AssignAssetHistory ahist = new AssignAssetHistory();
								ahist.setAssetId(asid);
								ahist.setEmpId(empDto.getEmployeeId());
								ahist.setAssettype(assetDto.getAssetType().getAssetType());

								ahist.setUpdateDate(dateFormatter.format(LocalDateTime.now()));
								ahist.setUpdateTime(timeFormatter.format(LocalDateTime.now()));
								ahist.setAssetName(assetDto.getAssetName());								
								ahist.setEmpName(managedEmp.getEmployeeName());

								assignassethistrepo.save(ahist);
							}
						}
					}
				}
				int output = 0;
				// If AssetDto to be assigned are smaller than the Already assigned assets
				if (nw_assets.length < ol_assets.length) {
//			List<String> olist= List.of(ol_assets);
//          List<String> nlist= List.of(nw_assets);

					List<String> olist = Arrays.asList(ol_assets);
					List<String> nlist = Stream.of(new_assets.split(",")).collect(Collectors.toList());
					for (int i = 0; i < ol_assets.length; i++) {
						if (nlist.contains(ol_assets[i])) {
							continue;
						} else {
							Long asid = Long.valueOf(ol_assets[i]);
							output = assignassetrepo.deleteAssignedAssetByEmpidAssetId(asid, empDto.getEmployeeId());

							if (output > 0) {
								AssetDto assetDto = getAssetDtoByIdUsingAssetClient(asid);
								int qty = assetDto.getQty();
								qty += 1;

								assetClient.updateAssetQuantitybyAssetId(asid, (Integer) qty);

								AssignAssetHistory ahist = new AssignAssetHistory();
								
								
								ahist.setAssetName(assetDto.getAssetName());
								ahist.setEmpName(managedEmp.getEmployeeName());
								ahist.setAssetId(asid);
								ahist.setEmpId(empDto.getEmployeeId());
								ahist.setAssettype(assetDto.getAssetType().getAssetType());

								ahist.setUpdateDate(dateFormatter.format(LocalDateTime.now()));
								ahist.setUpdateTime(timeFormatter.format(LocalDateTime.now()));
								
								assignassethistrepo.save(ahist);

							}
						}
					}
				}
				if (isassigned == null) {
					throw new GlobalException("Asset(s) are not Updated of Employee " + empDto.getEmployeeName());
				}
//			else {
//				throw new GlobalException("Asset(s) are not Updated of Employee "+emp.getEmp_name());
//			}
			}
		}
		if (res < 0) {
			throw new ResourceNotModifiedException("Employee", "ID", "" + empDto.getEmployeeId());
		}
	}

	private AssetDto getAssetDtoByIdUsingAssetClient(Long assetId) {
		ResponseEntity<AssetDto> assetById = assetClient.getAssetById(assetId);

		if (assetById != null) {
			return assetById.getBody();
		}
		return null;
	}
}
