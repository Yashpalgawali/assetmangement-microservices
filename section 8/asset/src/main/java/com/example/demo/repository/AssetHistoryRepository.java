package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssetUpdateHistory;

@Repository("assethistrepo")
public interface AssetHistoryRepository extends JpaRepository<AssetUpdateHistory, Long> {

	List<AssetUpdateHistory> findByAssetId(Long assetId);
}
