package com.example.demo.entity;

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
@Table(name = "tbl_assigned_asset_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignAssetHistory {

	@Id
	@SequenceGenerator(name = "assigned_asset_hist_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "assigned_asset_hist_seq", strategy = GenerationType.AUTO)
	Long assignedAssetHistory;

	Long empId;
	
	String empName;

	Long assetId;
	
	String assetName;
	
	String assettype;

	String assignedDate;

	String assignedTime;

	String updateDate;

	String updateTime;

	String assignedBy;

	String updatedBy;

}
