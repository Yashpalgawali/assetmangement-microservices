package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.DesignationDto;

public interface IDesignationService {

	public void createDesignation (DesignationDto designationDto);
	
	public DesignationDto getDesignationById(Long id);
	
	public List<DesignationDto> getAllDesignations();
	
	public DesignationDto getDesignationByName(String name);
	
	public void updateDesignation (DesignationDto designationDto);
}
