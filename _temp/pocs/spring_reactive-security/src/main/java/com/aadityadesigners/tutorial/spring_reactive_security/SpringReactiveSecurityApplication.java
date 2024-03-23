package com.aadityadesigners.tutorial.spring_reactive_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class SpringReactiveSecurityApplication {

	private static final Logger logger = LogManager.getLogger(SpringReactiveSecurityApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveSecurityApplication.class, args);
		logger.info("SpringReactiveSecurityApplication started successfully.");
	}

}
