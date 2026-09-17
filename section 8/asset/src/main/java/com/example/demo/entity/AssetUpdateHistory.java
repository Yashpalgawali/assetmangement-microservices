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
@Table(name = "tbl_asset_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetUpdateHistory {

	@Id
	@SequenceGenerator(name = "asset_hist_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "asset_hist_seq", strategy = GenerationType.AUTO)
	Long assetHistId;

	Long assetId;

	String assetType;

	String assetName;

	String modelNumber;

	String assetNumber;

	String assetConfiguration;

	String updatedBy;

	Long qty;

	String addedDate;

	String addedTime;

	String updationDate;

	String updationTime;

}
