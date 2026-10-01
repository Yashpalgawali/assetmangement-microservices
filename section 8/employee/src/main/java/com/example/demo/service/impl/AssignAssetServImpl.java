package com.example.demo.service.impl;

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
	public List<AssignAssetsDto> getAllAssignedAssets() {
		List<AssignAssets> assignedList = assignAssetRepo.findAll();
		
		Map<Long, String> collect = assignedList.stream().map(asset->{
			
			AssignAssetsDto dto = new AssignAssetsDto();
			EmployeeDto employee = empserv.getEmployeeById(null, asset.getEmployee().getEmployeeId());
			
			DepartmentDto departmentClient = deptClient.getDepartmentDto(null, asset.getEmployee().getDepartmentId());
			if(departmentClient !=null) {
				
				dto.setCompany(departmentClient.getCompanyName());
				dto.setDepartment(departmentClient.getDepartmentName());
			}
			else {
				dto.setCompany("");
				dto.setDepartment("");
			}
			
			ResponseEntity<AssetDto> assetBody = assetClient.getAssetById(asset.getAssetId());
			if(assetBody!=null) {
				AssetDto body = assetBody.getBody();
				dto.setAssetName(body.getAssetName());
				dto.setAssettype(body.getAssetType().getAssetType());
				dto.setEmpName(employee.getEmployeeName());
				
			}
			return dto;
			
		}).collect(Collectors.groupingBy(AssignAssetsDto::getEmpId,
				 Collectors.mapping(
						 AssignAssetsDto::getAssetName,
	                        Collectors.joining(",")
	                )
				));
		System.err.println("Collected Map result is "+collect.toString());
		return null;
	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssetsByEmpId(String correlationId, Long id) {
		System.err.println("Inside the getAllAssignedAssetsByEmpId service layer and EMP ID "+id);
		
		EmployeeDto foundEmp = empserv.getEmployeeById(correlationId, id);
		if(foundEmp!=null)
		{
			List<AssignAssets> assignedAssets = assignAssetRepo.findByEmployee(EmployeeMapper.mapToEmployee(foundEmp, new Employee()));		
					
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
