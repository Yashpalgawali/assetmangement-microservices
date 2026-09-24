package com.example.demo.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.AssetDto;

@FeignClient(name = "asset", fallback = AssetFallBack.class)
public interface AssetFeignClient {
	
	@GetMapping("/api/")
	public ResponseEntity<List<AssetDto>> getAllAssets();
	
	@GetMapping("/api/{id}")
	public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id);
}
