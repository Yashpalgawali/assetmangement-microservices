package com.example.demo.service.client;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.AssetDto;

@Component
public class AssetFallBack implements AssetFeignClient {

	@GetMapping("/{id}")
	public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) {

		AssetDto asset = new AssetDto();
		asset.setAssetName("");
		asset.setAssetNumber("");
		asset.setModelNumber("");
		asset.setQty(0);
		asset.setAssetType(null);
		return ResponseEntity.ok(asset);
	}

	@Override
	public ResponseEntity<List<AssetDto>> getAllAssets() {
		return ResponseEntity.ok(Collections.emptyList());
	}

}
