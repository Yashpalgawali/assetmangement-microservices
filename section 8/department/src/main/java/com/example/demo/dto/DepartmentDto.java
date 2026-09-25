package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDto {

	Long departmentId;
	
	@NotBlank(message = "Department Name can't be blank")
	String departmentName;
	
	Long companyId;
	
	String companyName;
	
}
