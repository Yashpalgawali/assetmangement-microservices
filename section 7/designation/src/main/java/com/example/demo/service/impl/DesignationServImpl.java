package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.DesignationDto;
import com.example.demo.entity.Designation;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.DesignationMapper;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.service.IDesignationService;

import lombok.RequiredArgsConstructor;

@Service("desigserv")
@RequiredArgsConstructor
public class DesignationServImpl implements IDesignationService {

	private final DesignationRepository desigrepo;
	
	@Override
	public void createDesignation(DesignationDto designationDto) {
		if(designationDto.getDesignationName().equals("")) {
			throw new GlobalException("Designation name can't be blank ");
		}
		String trimmedName= designationDto.getDesignationName().trim();
		Optional<Designation> found = desigrepo.findByDesignationName(trimmedName);
		if(found.isPresent()) {
			throw new ResourceAlreadyExistsException("Designation", "Name", trimmedName) ;
		}
		designationDto.setDesignationName(trimmedName);
		Designation desig =DesignationMapper.mapToDesignation(new Designation(), designationDto);
		
		if(desigrepo.save(desig) ==null ) {
			throw new GlobalException("Designation "+trimmedName+" is not created");
		}
		
	}

	@Override
	public DesignationDto getDesignationById(Long id) {
		
		return DesignationMapper.mapToDesignationDto(new DesignationDto(), desigrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Designation", "designation", ""+id) ));
	}

	@Override
	public List<DesignationDto> getAllDesignations() {
		
		var list = desigrepo.findAll();
		if(list.size() < 0)
			throw new ResourceNotFoundException("Designation", "designation", "designations");
		
		return list.stream().map((desig) -> {
			DesignationDto desigDto = new DesignationDto();
			
			desigDto.setDesignationId(desig.getDesignationId());
			desigDto.setDesignationName(desig.getDesignationName());
			
			return desigDto;
			
		}).collect(Collectors.toList());
	}

	@Override
	public DesignationDto getDesignationByName(String name) {

		if(name.equals(""))
			throw new GlobalException("Designation name can't be blank");
		Designation desig = desigrepo.findByDesignationName(name).orElseThrow(()-> new ResourceNotFoundException("Designation", "Name", name));
		return DesignationMapper.mapToDesignationDto(new DesignationDto(), desig);
		
	}

	@Override
	@Transactional
	public void updateDesignation(DesignationDto designationDto) {
		
		if(designationDto.getDesignationName().equals("")) {
			throw new GlobalException("Designation name can't be blank");
		}
		
		String trimmedName= designationDto.getDesignationName().trim();
		 
		designationDto.setDesignationName(trimmedName);
		Designation desig = DesignationMapper.mapToDesignation(new Designation(), designationDto);
		
		int result = desigrepo.updateDesignation(desig.getDesignationId(), desig.getDesignationName());
		if(result<0) {
			throw new ResourceNotModifiedException("Designation "+trimmedName+" is not updated");
		}

	}

}
