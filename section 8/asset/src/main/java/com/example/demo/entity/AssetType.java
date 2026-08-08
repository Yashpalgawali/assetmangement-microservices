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
@Table(name="tbl_asset_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AssetType {

	@Id
	@SequenceGenerator(name = "asset_type_seq",allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "asset_type_seq", strategy = GenerationType.AUTO)
	Long assetTypeId;
	
	String assetType;
}
