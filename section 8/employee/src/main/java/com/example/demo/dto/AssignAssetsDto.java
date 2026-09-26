package com.example.demo.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignAssetsDto {

	Long assignedAssetId;
	
	Long empId;

	String empName;
	
	String department;
	
	String company;

	List<Long> assetId;

	List<String> assetName;
	
	List<String> assettype;
}
