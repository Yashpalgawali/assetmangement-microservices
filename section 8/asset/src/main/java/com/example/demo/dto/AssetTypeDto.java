package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor @NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetTypeDto {

	Long assetTypeId;
	
	String assetType;	
	
}
