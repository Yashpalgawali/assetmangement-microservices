package com.example.demo.service.asset;

import java.util.List;

import com.example.demo.dto.AssetDto;

public interface IAssetService {

	public void createAsset(AssetDto Asset);
	
	public AssetDto getAssetById(Long id);
	
	public AssetDto getAssetByName(String name);
	
	public List<AssetDto> getAllAssets();
	
	public List<AssetDto> getAllAvailableAssets();
	
	public void updateAsset(AssetDto Asset);
	
	public void updateAssetQuantity(Long assetId);
		
	public Long getTotalAssetCount();
}
