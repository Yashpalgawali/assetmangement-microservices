package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name ="tbl_employee")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

	@Id
	@SequenceGenerator(name= "emp_seq",allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "emp_seq", strategy = GenerationType.AUTO)
	Long empId;
	
	String empName;
	
	Long department;
	
	Long company;
}
