package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AssetDto;
import com.example.demo.entity.Asset;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.AssetMapper;
import com.example.demo.repository.AssetRepository;
import com.example.demo.service.asset.IAssetService;

import lombok.RequiredArgsConstructor;

@Service("assetserv")
@RequiredArgsConstructor
public class AssetServImpl implements IAssetService {

	private final AssetRepository assetrepo;
	
	@Override
	public void createAsset(AssetDto assetDto) {
		
		String trimmedName = assetDto.getAssetName();
		Optional<Asset> a = assetrepo.findByAssetName(trimmedName);
		 if(a.isPresent()) {
			 throw new ResourceAlreadyExistsException("Asset  "+assetDto.getAssetName()+" already exists");
		 }
		 
		 Asset mappedAsset = AssetMapper.mapToAsset(assetDto, new Asset());
		 
		 Asset savedAsset =  assetrepo.save(mappedAsset);
		 if(savedAsset== null) {
			 throw new GlobalException("Asset  "+assetDto.getAssetName()+" is not created");
		 }		
	}

	@Override
	public AssetDto getAssetById(Long id) {
		
		Asset found = assetrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", ""+id) );
		
		return AssetMapper.mapToAssetDto(found, new AssetDto());
	}

	@Override
	public AssetDto getAssetByName(String name) {
		
		Asset found = assetrepo.findByAssetName(name).orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", name) );
		
		return AssetMapper.mapToAssetDto(found, new AssetDto());
	}

	@Override
	public List<AssetDto> getAllAssets() {
		
		var assetList = assetrepo.findAll();
		if(assetList.size()<0) {
			throw new ResourceNotFoundException("Asset ", "List", "asset ");
		}
		List<AssetDto> assetTypeDtoList = assetList.stream().map(a-> {
			AssetDto mapped = AssetMapper.mapToAssetDto(a, new AssetDto());
			
			return mapped;
			
		}).collect(Collectors.toList());
		return assetTypeDtoList;
	}

	@Override
	@Transactional
	public void updateAsset(AssetDto assetDto) {

		assetrepo.findById(assetDto.getAssetId()).orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", ""+assetDto.getAssetId()) );

		String trimmedName = assetDto.getAssetName();
		Optional<Asset> a = assetrepo.findByAssetName(trimmedName);
		 if(a.isPresent()) {
			 throw new ResourceAlreadyExistsException("Asset  "+assetDto.getAssetName()+" already exists");
		 }

		int res = assetrepo.updateAsset(assetDto.getAssetId(), assetDto.getAssetName().trim(),assetDto.getModelNumber().trim(),assetDto.getAssetNumber().trim(),assetDto.getAssetType().getAssetTypeId(),assetDto.getQty());
		if(res < 0 ) {
			throw new ResourceNotModifiedException("Asset ", "name", assetDto.getAssetName());
		}
    }

}
