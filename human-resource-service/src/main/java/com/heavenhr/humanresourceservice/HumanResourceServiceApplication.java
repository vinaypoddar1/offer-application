package com.heavenhr.humanresourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class HumanResourceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourceServiceApplication.class, args);
	}
}
