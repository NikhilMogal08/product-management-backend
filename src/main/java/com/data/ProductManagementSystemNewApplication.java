package com.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProductManagementSystemNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementSystemNewApplication.class, args);
	}


	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ProductManagementSystemNewApplication.class);
	}
}


