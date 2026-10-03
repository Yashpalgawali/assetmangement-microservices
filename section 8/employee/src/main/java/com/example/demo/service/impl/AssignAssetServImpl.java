package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AssetDto;
import com.example.demo.dto.AssignAssetsDto;
import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.AssignAssets;
import com.example.demo.entity.Employee;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.AssignAssetRepository;
import com.example.demo.service.IAssignAssetsService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.client.AssetFeignClient;
import com.example.demo.service.client.DepartmentFeignClient;

import lombok.RequiredArgsConstructor;

@Service("assignassetserv")
@RequiredArgsConstructor
public class AssignAssetServImpl implements IAssignAssetsService {

	private final AssignAssetRepository assignAssetRepo;
	
	private final IEmployeeService empserv;
	
	private final AssetFeignClient assetClient;
	
	private final DepartmentFeignClient deptClient;
	
	@Override
	public void assignAssets(AssignAssetsDto assignAssetsDto) {
		

	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssets(String correlationId) {
		 List<AssignAssets> assignedList = assignAssetRepo.findAll();

		    Map<Long, AssignAssetsDto> employeeMap = new LinkedHashMap<>();

		    for (AssignAssets asset : assignedList) {

		        Long empId = asset.getEmployee().getEmployeeId();

		        AssignAssetsDto dto = employeeMap.get(empId);

		        if (dto == null) {

		            dto = new AssignAssetsDto();
		            dto.setEmpId(empId);

		            EmployeeDto employee =
		                    empserv.getEmployeeById(correlationId, empId);

		            if (employee != null) {
		                dto.setEmpName(employee.getEmployeeName());
		            }

		            DepartmentDto department =
		                    deptClient.getDepartmentDto(
		                            correlationId,
		                            asset.getEmployee().getDepartmentId()
		                    );

		            if (department != null) {
		                dto.setCompany(department.getCompanyName());
		                dto.setDepartment(department.getDepartmentName());
		            } else {
		                dto.setCompany("");
		                dto.setDepartment("");
		            }

		            dto.setAssetName("");

		            employeeMap.put(empId, dto);
		        }

		        ResponseEntity<AssetDto> assetBody =
		                assetClient.getAssetById(asset.getAssetId());

		        if (assetBody != null && assetBody.getBody() != null) {

		            AssetDto body = assetBody.getBody();

		            String currentAssets = dto.getAssetName();
		            String currentAssetTypes = dto.getAssettype();
		            
		            if (currentAssets == null || currentAssets.isEmpty()) {
		            	
		                dto.setAssetName(body.getAssetName());
		            } else {
		            	dto.setAssettype(currentAssetTypes + ","+ body.getAssetType() );
		                dto.setAssetName(
		                        currentAssets + "," + body.getAssetName()
		                );
		            }

		            if (body.getAssetType() != null) {
		            	if (currentAssets == null || currentAssets.isEmpty()) {
		            		dto.setAssettype(body.getAssetType().getAssetType());
		            	}
		            	else {
		            		dto.setAssettype(currentAssetTypes+","+body.getAssetType().getAssetType());
		            	}
		            	
//		                dto.setAssettype(
//		                        body.getAssetType().getAssetType()
//		                );
		            }
		        }
		    }

		    return new ArrayList<>(employeeMap.values());
//		List<AssignAssets> assignedList = assignAssetRepo.findAll();
//		
//		Map<Long, String> collect = assignedList.stream().map(asset->{
//			
//			AssignAssetsDto dto = new AssignAssetsDto();
//			EmployeeDto employee = empserv.getEmployeeById(correlationId, asset.getEmployee().getEmployeeId());
//			
//			DepartmentDto departmentClient = deptClient.getDepartmentDto(correlationId, asset.getEmployee().getDepartmentId());
//			if(departmentClient !=null) {
//				System.err.println("Department Client is called "+departmentClient.toString());
//				dto.setCompany(departmentClient.getCompanyName());
//				dto.setDepartment(departmentClient.getDepartmentName());
//			}
//			else {
//				System.err.println("Department Client is NOT Called ");
//				dto.setCompany("");
//				dto.setDepartment("");
//			}
//			
//			ResponseEntity<AssetDto> assetBody = assetClient.getAssetById(asset.getAssetId());
//			if(assetBody!=null) {
//				
//				System.err.println("Asset Client is called "+assetBody.toString());
//				AssetDto body = assetBody.getBody();
//				dto.setAssetName(body.getAssetName());
//				dto.setAssettype(body.getAssetType().getAssetType());
//				dto.setEmpName(employee.getEmployeeName());
//				
//			}
//			return dto;
//			
//		}).collect(Collectors.groupingBy(AssignAssetsDto::getEmpId,
//				 Collectors.mapping(
//						 AssignAssetsDto::getAssetName,
//	                        Collectors.joining(",")
//	                )
//				));
//		System.err.println("Collected Map result is "+collect.toString());
//		return null;
	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssetsByEmpId(String correlationId, Long id) {
		System.err.println("Inside the getAllAssignedAssetsByEmpId service layer and EMP ID "+id);
		
		EmployeeDto foundEmp = empserv.getEmployeeById(correlationId, id);
		if(foundEmp!=null)
		{
			List<AssignAssets> assignedAssets = assignAssetRepo.findByEmployee(EmployeeMapper.mapToEmployee(foundEmp, new Employee()));		
			List<String> astList = new ArrayList<>();
			return assignedAssets.stream().map(assets -> {
				AssignAssetsDto assetDto = new AssignAssetsDto();
				deptClient.getDepartmentDto(correlationId, id);
				ResponseEntity<AssetDto> assetById = assetClient.getAssetById(assets.getAssetId());

				if(assetById.getBody()!=null) {
					AssetDto astDto = assetById.getBody();
					assetDto.setAssetName(astDto.getAssetName());
					
					assetDto.setAssettype(astDto.getAssetType().getAssetType());
				}
				else {
					assetDto.setAssetName("");
					assetDto.setAssettype("");
				}
//				List<Long> asids= new ArrayList<>();
//				asids.add(assets.getAssetId());
				
				astList.add(String.valueOf(assets.getAssetId()));
				assetDto.setAssetId(astList);
//				assetDto.setAssetId(asids);
				assetDto.setAssignedAssetId(assets.getAssignedAssetId());
				assetDto.setEmpName(foundEmp.getEmployeeName());
				assetDto.setEmpId(foundEmp.getEmployeeId());
				
				System.err.println("Assigned Assets DTO result is "+assetDto.toString());
				return assetDto;
				
			}).collect(Collectors.toList());
		}
		throw new ResourceNotFoundException("Employee", "Id", ""+id );
		
	}

}
