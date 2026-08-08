package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_asset")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Asset {

	@Id
	@SequenceGenerator(name = "asset_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "asset_seq", strategy = GenerationType.AUTO)
	Long assetId;

	String assetName;

	String modelNumber;

	String assetNumber;
	
	Integer qty;

	@ManyToOne
	@JoinColumn(name = "asset_type_id")
	AssetType assetType;
}
