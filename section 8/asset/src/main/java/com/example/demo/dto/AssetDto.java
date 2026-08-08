package com.example.demo.dto;

import com.example.demo.entity.AssetType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor @NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetDto {
	
	Long assetId;
	
	String assetName;
	
	String modelNumber;
	
	String assetNumber;
	
	Integer qty;
	
	AssetType assetType;
}
