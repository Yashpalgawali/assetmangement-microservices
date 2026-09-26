package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssignAssets;
import com.example.demo.entity.Employee;


@Repository("assignassetrepo")
public interface AssignAssetRepository extends JpaRepository<AssignAssets, Long> {

	 List<AssignAssets> findByEmployee(Employee employee);
}
