package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Asset;


@Repository("assetrepo")
public interface AssetRepository extends JpaRepository<Asset, Long> {

	@Query("UPDATE Asset a SET a.assetName=:name, a.modelNumber=:model,a.assetNumber=:number, a.assetType.assetTypeId=:typeid, a.qty=:qty WHERE a.assetId=:id")
	@Modifying
	public int updateAsset(Long id, String name,String model,String number,Long typeid,Integer qty);
	
	@Query("UPDATE Asset a SET a.qty=:qty WHERE a.assetId=:id")
	@Modifying
	public int UpdateAssetQty(Long id, Integer qty);
	
	Optional<Asset> findByAssetName(String assetName);
	
	Optional<Asset> findByAssetNumber(String assetNumber);
	
	Optional<Asset> findByModelNumber(String modelNumber);
}
