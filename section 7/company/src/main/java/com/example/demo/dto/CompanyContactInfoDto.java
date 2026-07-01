package com.example.demo.dto;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "company")
@Data @AllArgsConstructor @NoArgsConstructor
public class CompanyContactInfoDto {

	private String message;
	
	private List<String> onCallSupport;
	
	private Map<String, String> contactDetails;
}
