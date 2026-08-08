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

import com.example.demo.dto.AssetDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.asset.IAssetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("asset/api")
@RequiredArgsConstructor
public class AssetController {

	private final IAssetService assetServ;

	@PostMapping("/")
	public ResponseEntity<ResponseDto> createAssetType(@RequestBody AssetDto assetDto) {

		assetServ.createAsset(assetDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(
				new ResponseDto("Asset " + assetDto.getAssetName() + " is created successfully", HttpStatus.CREATED));
	}

	@GetMapping("/")
	public ResponseEntity<List<AssetDto>> getAllAssets() {

		var list = assetServ.getAllAssets();

		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) {

		var asset = assetServ.getAssetById(id);

		return ResponseEntity.status(HttpStatus.OK).body(asset);
	}

	@GetMapping("/{name}")
	public ResponseEntity<AssetDto> getAssetByName(@PathVariable String name) {

		var asset = assetServ.getAssetByName(name);

		return ResponseEntity.status(HttpStatus.OK).body(asset);
	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateAssetType(@RequestBody AssetDto assetDto) {

		assetServ.updateAsset(assetDto);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto("Asset " + assetDto.getAssetName() + " is updated successfully", HttpStatus.OK));
	}
}
