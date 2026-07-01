package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo.dto.DesignationContactInfoDto;

@SpringBootApplication
@EnableConfigurationProperties(value = DesignationContactInfoDto.class)
public class DesignationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignationApplication.class, args);
	}

}
