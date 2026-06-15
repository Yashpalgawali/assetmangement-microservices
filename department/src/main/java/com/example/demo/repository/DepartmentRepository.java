package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Department;

@Repository("deptrepo")
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	@Query("UPDATE Deaprtment d SET d.departmentName=:deptName,d.companyId=:companyId WHERE d.departmentId=:deptId")
	@Modifying
	public int updateDepartment(Long deptId,String deptName,Long companyId);
	
	Optional<Department> findByDepartmentName(String departmentName);
	
	List<Department> findByCompanyId(Long companyId);
}
