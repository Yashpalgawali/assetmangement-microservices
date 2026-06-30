package com.example.demo.entity;

import jakarta.persistence.Column;
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
@Table(name= "tbl_designation")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Designation {

	@Id
	@SequenceGenerator(name="desig_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "desig_seq", strategy = GenerationType.AUTO)
	Long designationId;
	
	@Column(unique = true)
	String designationName;
	
}
