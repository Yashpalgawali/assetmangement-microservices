package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo.dto.CompanyContactInfoDto;

@SpringBootApplication
@EnableConfigurationProperties(value = CompanyContactInfoDto.class)
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}

}
