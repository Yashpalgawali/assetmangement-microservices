package com.example.demo.service.asset.history;

import java.util.List;

import com.example.demo.entity.AssetUpdateHistory;

public interface IAssetUpdateHistoryService {

	public void createAssetUpdate(AssetUpdateHistory asstHistory);
	
	public List<AssetUpdateHistory> getAllAssetUpdateHistory();
	
	public List<AssetUpdateHistory> getAllAssetUpdateHistoryByAssetid(Long assetId);
}
