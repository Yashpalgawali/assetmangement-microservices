package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_company")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@SequenceGenerator(name = "company_seq" ,allocationSize = 1, initialValue = 1)
public class Company {

	@Id
	@GeneratedValue(generator = "company_seq",strategy = GenerationType.AUTO)
	Long companyId;
	
	@Column(unique = true)
	@Size(min = 2, max=40 , message="Company Name must have at least 2 or 40 characters")
	@NotBlank(message = "Company Name can't be blank")
	String companyName;
	
	boolean communicationSw;
}
