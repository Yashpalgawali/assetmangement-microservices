package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssignAssets;

@Repository("assignassetrepo")
public interface AssingAssetRepository extends JpaRepository<AssignAssets, Long> {

}
