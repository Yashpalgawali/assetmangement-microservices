package com.example.demo.service.asset.history.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AssetUpdateHistory;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetHistoryRepository;
import com.example.demo.service.asset.history.IAssetUpdateHistoryService;

import lombok.RequiredArgsConstructor;

@Service("assethistserv")
@RequiredArgsConstructor
public class AssetUpdateHistoryServImpl implements IAssetUpdateHistoryService {

	private final AssetHistoryRepository assethistrepo;

	@Override
	public void createAssetUpdate(AssetUpdateHistory assetHistory) {

		assethistrepo.save(assetHistory);

	}

	@Override
	public List<AssetUpdateHistory> getAllAssetUpdateHistory() {

		var list = assethistrepo.findAll();

		if (list.size() > 0) {
			return list;
		}
		throw new ResourceNotFoundException("Asset ", "History", null);
	}

	@Override
	public List<AssetUpdateHistory> getAllAssetUpdateHistoryByAssetid(Long assetId) {

		var list = assethistrepo.findByAssetId(assetId);
		if (list.size() > 0) {
			return list;
		}
		throw new ResourceNotFoundException("Asset ", "History", "List");
	}

}
