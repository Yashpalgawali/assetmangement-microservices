package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssetType;

@Repository("assettyperepo")
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

	public Optional<AssetType> findByAssetType(String assetType);
	
	@Query("UPDATE AssetType a SET a.assetType=:type WHERE a.assetTypeId=:id")
	@Modifying
	public int UpdateAssetType(Long id, String type);
}
