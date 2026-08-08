package com.example.demo.service.assettype;

import java.util.List;

import com.example.demo.dto.AssetTypeDto;

public interface IAssetTypeService {

	public void createAssetType(AssetTypeDto assetType);
	
	public AssetTypeDto getAssetTypeById(Long id);
	
	public AssetTypeDto getAssetTypeByName(String name);
	
	public List<AssetTypeDto> getAllAssetTypes();
	
	public void updateAssetType(AssetTypeDto assetType);
}
