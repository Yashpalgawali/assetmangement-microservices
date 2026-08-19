package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import java.util.List;



@Repository("emprepo")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	public Optional<Employee> findByEmpName(String empName);
	
	List<Employee> findByDepartment(Long department);
	
	List<Employee> findByCompany(Long company);
	
	@Query("UPDATE Employee e SET e.empName=:name,e.department=:deptid,e.company=:compid WHERE e.empId=:empid")
	@Modifying
	public int updateEmployee(Long empid,String name, Long deptid, Long compid);
}
