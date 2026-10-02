package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.AssignAssets;
import com.example.demo.entity.Employee;

@Repository("assignassetrepo")
public interface AssignAssetRepository extends JpaRepository<AssignAssets, Long> {

	List<AssignAssets> findByEmployee(Employee employee);

	@Query("SELECT tas FROM AssignAssets tas JOIN tas.employee WHERE tas.employee.employeeId=:eid")
	public List<AssignAssets> getAllAssignedAssetsByEmpId(Long eid);

	@Modifying
	@Transactional
	@Query("DELETE FROM AssignAssets a  WHERE a.assetId=:assetid AND a.employee.employeeId=:empid")
	public int deleteAssignedAssetByEmpidAssetId(Long assetid, Long empid);

}
