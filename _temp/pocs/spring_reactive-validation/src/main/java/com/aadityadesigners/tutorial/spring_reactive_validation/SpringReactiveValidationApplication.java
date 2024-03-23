package com.aadityadesigners.tutorial.spring_reactive_validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class SpringReactiveValidationApplication {

	private static final Logger logger = LogManager.getLogger(SpringReactiveValidationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveValidationApplication.class, args);
		logger.info("SpringReactiveValidationApplication started successfully.");
	}

}
