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
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_department")
@SequenceGenerator(name="dept_seq",allocationSize = 1,initialValue = 1)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Department {

	@Id
	@GeneratedValue(generator = "dept_seq" , strategy = GenerationType.AUTO)
	Long departmentId;
	
	String departmentName;
	
	Long companyId;
}
