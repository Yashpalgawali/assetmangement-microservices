package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AssignAssetsDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.AssignAssets;
import com.example.demo.entity.Employee;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.AssignAssetRepository;
import com.example.demo.service.IAssignAssetsService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.client.AssetFeignClient;

import lombok.RequiredArgsConstructor;

@Service("assignassetserv")
@RequiredArgsConstructor
public class AssignAssetServImpl implements IAssignAssetsService {

	private final AssignAssetRepository assignAssetRepo;
	
	private final IEmployeeService empserv;
	
	private final AssetFeignClient assetClient;
	
	@Override
	public void assignAssets(AssignAssetsDto assignAssetsDto) {
		

	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssetsByEmpId(String correlationId, Long id) {
		
		EmployeeDto foundEmp = empserv.getEmployeeById(correlationId, id);
		List<AssignAssets> assignedAssets = assignAssetRepo.findByEmployee(EmployeeMapper.mapToEmployee(foundEmp, new Employee()));		
		
		return assignedAssets.stream().map(assets -> {
			AssignAssetsDto assetDto = new AssignAssetsDto();
			
			assetDto.setAssignedAssetId(assets.getAssignedAssetId());
			
			return assetDto;
			
		}).collect(Collectors.toList());
		
	}

}
