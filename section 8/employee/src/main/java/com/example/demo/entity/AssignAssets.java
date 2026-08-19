package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name ="tbl_assigned_assets")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignAssets {

	@Id
	@SequenceGenerator(name="assigned_asset_seq", initialValue = 1,allocationSize = 1)
	@GeneratedValue(generator = "assigned_asset_seq",strategy = GenerationType.AUTO)
	Long assignedAssetId;
	
	Long empId;
	
	Long assetId;
	
	
	
}
