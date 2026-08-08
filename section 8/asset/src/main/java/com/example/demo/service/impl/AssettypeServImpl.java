package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AssetTypeDto;
import com.example.demo.entity.AssetType;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.AssetTypeMapper;
import com.example.demo.repository.AssetTypeRepository;
import com.example.demo.service.assettype.IAssetTypeService;

import lombok.RequiredArgsConstructor;

@Service("assettypeserv")
@RequiredArgsConstructor
public class AssettypeServImpl implements IAssetTypeService {

	private final AssetTypeRepository atyperepo;

	@Override
	public void createAssetType(AssetTypeDto assetTypeDto) {

		Optional<AssetType> atype = atyperepo.findByAssetType(assetTypeDto.getAssetType());
		if (atype.isPresent()) {
			throw new ResourceAlreadyExistsException("Asset type " + assetTypeDto.getAssetType() + " already exists");
		}

		AssetType mappedAssetType = AssetTypeMapper.maptToAssetType(assetTypeDto, new AssetType());

		AssetType savedAssetType = atyperepo.save(mappedAssetType);
		if (savedAssetType == null) {
			throw new GlobalException("Asset type " + assetTypeDto.getAssetType() + " is not created");
		}

	}

	@Override
	public AssetTypeDto getAssetTypeById(Long id) {

		AssetType found = atyperepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Asset type ", "ID", "" + id));

		return AssetTypeMapper.maptToAssetTypeDto(found, new AssetTypeDto());
	}

	@Override
	public AssetTypeDto getAssetTypeByName(String name) {

		AssetType found = atyperepo.findByAssetType(name)
				.orElseThrow(() -> new ResourceNotFoundException("Asset type ", "ID", name));

		return AssetTypeMapper.maptToAssetTypeDto(found, new AssetTypeDto());
	}

	@Override
	public List<AssetTypeDto> getAllAssetTypes() {

		List<AssetType> assetTypeList = atyperepo.findAll();

		System.err.println("asset type list is " + assetTypeList.size());

		if (assetTypeList.size() < 0) {
			throw new ResourceNotFoundException("Asset Type", "List", "asset types");
		} else {
			List<AssetTypeDto> assetTypeDtoList = assetTypeList.stream().map(atype -> {
				AssetTypeDto mapped = AssetTypeMapper.maptToAssetTypeDto(atype, new AssetTypeDto());

				return mapped;

			}).collect(Collectors.toList());
			return assetTypeDtoList;
		}
	}

	@Override
	@Transactional
	public void updateAssetType(AssetTypeDto assetType) {

		atyperepo.findById(assetType.getAssetTypeId())
				.orElseThrow(() -> new ResourceNotFoundException("Asset type ", "ID", "" + assetType.getAssetTypeId()));

		int res = atyperepo.UpdateAssetType(assetType.getAssetTypeId(), assetType.getAssetType());
		if (res < 0) {
			throw new ResourceNotModifiedException("Asset Type", "name", assetType.getAssetType());
		}
	}

}
