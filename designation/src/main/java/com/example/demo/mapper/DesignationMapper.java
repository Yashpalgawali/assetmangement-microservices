package com.example.demo.mapper;

import com.example.demo.dto.DesignationDto;
import com.example.demo.entity.Designation;

public class DesignationMapper {

	public static Designation mapToDesignation(Designation designation, DesignationDto designationDto) {
		
		designation.setDesignationId(designationDto.getDesignationId());
		designation.setDesignationName(designationDto.getDesignationName());
		return designation;
	}
	
	public static DesignationDto mapToDesignationDto(DesignationDto designationDto, Designation designation) {
		
		designationDto.setDesignationId(designation.getDesignationId());
		designationDto.setDesignationName(designation.getDesignationName());
		return designationDto;
	}
}
