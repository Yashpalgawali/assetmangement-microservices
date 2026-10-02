package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AssetDto;
import com.example.demo.dto.AssetTypeDto;
import com.example.demo.entity.Asset;
import com.example.demo.entity.AssetType;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.AssetMapper;
import com.example.demo.mapper.AssetTypeMapper;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.AssetTypeRepository;
import com.example.demo.service.asset.IAssetService;

import lombok.RequiredArgsConstructor;

@Service("assetserv")
@RequiredArgsConstructor
public class AssetServImpl implements IAssetService {

	private final AssetRepository assetrepo;

	private final AssetTypeRepository assetTypeRepo;

	@Override
	public void createAsset(AssetDto assetDto) {

		String trimmedName = assetDto.getAssetName();
		Optional<Asset> a = assetrepo.findByAssetName(trimmedName);
		if (a.isPresent()) {
			throw new ResourceAlreadyExistsException("Asset  " + assetDto.getAssetName() + " already exists");
		}

		Asset mappedAsset = AssetMapper.mapToAsset(assetDto, new Asset());
		mappedAsset.setAssetId(null);

		Optional<AssetType> foundAssetType = assetTypeRepo.findById(assetDto.getAssetType().getAssetTypeId());
		mappedAsset.setAssetType(foundAssetType.get());

		Asset savedAsset = assetrepo.save(mappedAsset);

		if (savedAsset == null) {
			throw new GlobalException("Asset  " + assetDto.getAssetName() + " is not created");
		}
	}

	@Override
	public AssetDto getAssetById(Long id) {

		Asset found = assetrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", "" + id));
		AssetType assetType = assetTypeRepo.findById(found.getAssetType().getAssetTypeId()).orElseThrow(
				() -> new ResourceNotFoundException("Asset Type", "Id", "" + found.getAssetType().getAssetTypeId()));

		AssetDto mappedAssetDto = AssetMapper.mapToAssetDto(found, new AssetDto());
		System.err.println("Mapped Asset DTO " + mappedAssetDto.toString());

		mappedAssetDto.setAssetType(assetType);

		System.err.println("mappedAssetDTO after adding assetType in DTO " + mappedAssetDto.toString());

		return mappedAssetDto;
	}

	@Override
	public AssetDto getAssetByName(String name) {

		Asset found = assetrepo.findByAssetName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", name));

		AssetType assetType = assetTypeRepo.findById(found.getAssetType().getAssetTypeId()).orElseThrow(
				() -> new ResourceNotFoundException("Asset Type", "Id", "" + found.getAssetType().getAssetTypeId()));

		found.setAssetType(assetType);
		return AssetMapper.mapToAssetDto(found, new AssetDto());
	}

	@Override
	public List<AssetDto> getAllAssets() {

		var assetList = assetrepo.findAll();

		if (assetList.size() < 0) {
			throw new ResourceNotFoundException("Asset ", "List", "asset ");
		}
		List<AssetDto> assetDtoList = assetList.stream().map(a -> {

			return AssetMapper.mapToAssetDto(a, new AssetDto());

		}).collect(Collectors.toList());

		assetDtoList.forEach(System.err::print);
		return assetDtoList;
	}

	@Override
	@Transactional
	public void updateAsset(AssetDto assetDto) {

		assetrepo.findById(assetDto.getAssetId())
				.orElseThrow(() -> new ResourceNotFoundException("Asset ", "ID", "" + assetDto.getAssetId()));

		String trimmedName = assetDto.getAssetName();

		int res = assetrepo.updateAsset(assetDto.getAssetId(), trimmedName, assetDto.getModelNumber().trim(),
				assetDto.getAssetNumber().trim(), assetDto.getAssetType().getAssetTypeId(), assetDto.getQty());
		if (res < 0) {
			throw new ResourceNotModifiedException("Asset ", "name", assetDto.getAssetName());
		}
	}

	@Override
	public Long getTotalAssetCount() {
		System.err.println("Assets count is " + assetrepo.count());
		return assetrepo.count();
	}

	@Override
	public List<AssetDto> getAllAvailableAssets() {
		var assetList = assetrepo.getAllAvailableAssets();
		if (assetList.size() < 0) {
			throw new ResourceNotFoundException("Asset ", "List", "asset ");
		}
		List<AssetDto> assetDtoList = assetList.stream().map(a -> {

			return AssetMapper.mapToAssetDto(a, new AssetDto());

		}).collect(Collectors.toList());
		System.err.println("Assets with qauntity more than 0");
		assetDtoList.forEach(System.err::print);
		return assetDtoList;
	}

	@Override
	@Transactional
	public void updateAssetQuantity(Long assetId) {
		AssetDto dto = this.getAssetById(assetId);

		int res = assetrepo.UpdateAssetQty(assetId, dto.getQty() - 1);
		if (res < 0) {
			throw new ResourceNotModifiedException("Asset", "Quantiy", "" + assetId);
		}

	}

	@Override
	public void updateAssetQuantityByAssetId(Long assetId, Integer qty) {

		AssetDto dto = this.getAssetById(assetId);

		int res = assetrepo.UpdateAssetQty(assetId, qty);
		if (res < 0) {
			throw new ResourceNotModifiedException("Asset", "Quantiy", "" + assetId);
		}
	}

}
