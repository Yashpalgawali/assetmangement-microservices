package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Designation;

@Repository("desigrepo")
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	Optional<Designation> findByDesignationName(@Param("name") String name);

	@Query("UPDATE Designation d SET d.designationName=:name WHERE d.designationId=:id")
	@Modifying
	public int updateDesignation(Long id, String name);
}
