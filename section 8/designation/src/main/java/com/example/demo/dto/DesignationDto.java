package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor

public class DesignationDto {
	
	Long designationId;
	
	@NotBlank(message = "Designation name can't be blank")
	@NotNull(message =  "Designation name can't be null")
	String designationName;
}
