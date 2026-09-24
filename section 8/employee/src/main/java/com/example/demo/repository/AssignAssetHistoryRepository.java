package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssignAssetHistory;
import java.util.List;


@Repository("assignassethistrepo")
public interface AssignAssetHistoryRepository extends JpaRepository<AssignAssetHistory, Long> {

	List<AssignAssetHistory> findByEmpId(Long empId);
	
	List<AssignAssetHistory> findByEmpName(String empName);
}
