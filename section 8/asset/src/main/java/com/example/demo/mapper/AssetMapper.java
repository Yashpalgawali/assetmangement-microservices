package com.example.demo.mapper;

import com.example.demo.dto.AssetDto;
import com.example.demo.entity.Asset;

public class AssetMapper {

	public static Asset mapToAsset(AssetDto assetDto, Asset asset) {

		asset.setAssetName(assetDto.getAssetName().trim());
		asset.setModelNumber(assetDto.getModelNumber().trim());
		asset.setAssetNumber(assetDto.getAssetNumber().trim());
		asset.setAssetType(assetDto.getAssetType());
		asset.setQty(assetDto.getQty());

		return asset;
	}

	public static AssetDto mapToAssetDto(Asset asset, AssetDto assetDto) {

		assetDto.setAssetId(asset.getAssetId());
		assetDto.setAssetName(asset.getAssetName().trim());
		assetDto.setModelNumber(asset.getModelNumber().trim());
		assetDto.setAssetNumber(asset.getAssetNumber().trim());
		assetDto.setAssetType(asset.getAssetType());
		assetDto.setQty(asset.getQty());

		return assetDto;
	}
}
