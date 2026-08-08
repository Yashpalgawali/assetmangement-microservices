package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AssetTypeDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.assettype.IAssetTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assettype/api")
@RequiredArgsConstructor
public class AssetTypeController {

	private final IAssetTypeService assetTypeServ;

	@PostMapping("/")
	public ResponseEntity<ResponseDto> createAssetType(@RequestBody AssetTypeDto assetTypeDto) {

		assetTypeServ.createAssetType(assetTypeDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(
				"Asset Type " + assetTypeDto.getAssetType() + " is created successfully", HttpStatus.CREATED));
	}

	@GetMapping("/")
	public ResponseEntity<List<AssetTypeDto>> getAllAssetTypes() {

		var list = assetTypeServ.getAllAssetTypes();

		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AssetTypeDto> getAssetTypeById(@PathVariable Long id) {

		var assetType = assetTypeServ.getAssetTypeById(id);

		return ResponseEntity.status(HttpStatus.OK).body(assetType);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<AssetTypeDto> getAssetTypeByName(@PathVariable String name) {

		var assetType = assetTypeServ.getAssetTypeByName(name);

		return ResponseEntity.status(HttpStatus.OK).body(assetType);
	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateAssetType(@RequestBody AssetTypeDto assetTypeDto) {

		assetTypeServ.updateAssetType(assetTypeDto);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(
				"Asset Type " + assetTypeDto.getAssetType() + " is updated successfully", HttpStatus.OK));
	}
}
