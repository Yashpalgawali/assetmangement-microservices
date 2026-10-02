package com.example.demo.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.dto.AssignAssetsDto;

public interface IAssignAssetsService {

	public void assignAssets(AssignAssetsDto as);
	
	public List<AssignAssetsDto> getAllAssignedAssets(String correlationId);
	
	public List<AssignAssetsDto> getAllAssignedAssetsByEmpId(String correlationId, Long id);
	
	
	
}
