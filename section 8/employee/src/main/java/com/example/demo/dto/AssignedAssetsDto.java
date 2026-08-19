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
public class AssignedAssetsDto {

	Long empId;
	
	List<Long> assetId;
	
	String empName;
	
	String department;
	
	String company;
	
	String assetName;
	
	String assettype;
}
