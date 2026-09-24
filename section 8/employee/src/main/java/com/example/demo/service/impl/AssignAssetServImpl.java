package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AssignAssetsDto;
import com.example.demo.repository.AssignAssetRepository;
import com.example.demo.service.IAssignAssetsService;

import lombok.RequiredArgsConstructor;

@Service("assignassetserv")
@RequiredArgsConstructor
public class AssignAssetServImpl implements IAssignAssetsService {

	private final AssignAssetRepository assignAssetRepo;
	
	@Override
	public void assignAssets(AssignAssetsDto assignAssetsDto) {
		

	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssignAssetsDto> getAllAssignedAssetsByEmpId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
