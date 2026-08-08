package com.example.demo.mapper;

import com.example.demo.dto.AssetTypeDto;
import com.example.demo.entity.AssetType;

public class AssetTypeMapper {

	public static AssetType maptToAssetType(AssetTypeDto assetTypeDto, AssetType assetType) {
		assetType.setAssetTypeId(assetTypeDto.getAssetTypeId());
		assetType.setAssetType(assetTypeDto.getAssetType());		
		return assetType;
	}
	
	public static AssetTypeDto maptToAssetTypeDto(AssetType assetType, AssetTypeDto assetTypeDto) {
		assetTypeDto.setAssetTypeId(assetType.getAssetTypeId());
		assetTypeDto.setAssetType(assetType.getAssetType());		
		return assetTypeDto;
	}
}
