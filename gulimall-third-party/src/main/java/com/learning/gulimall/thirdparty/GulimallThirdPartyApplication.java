package com.learning.gulimall.thirdparty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.learning.gulimall.thirdparty"})
public class GulimallThirdPartyApplication {
	private static final Logger logger = LoggerFactory.getLogger(GulimallThirdPartyApplication.class);

	public static void main(String[] args) {
		logger.info("Starting GulimallThirdPartyApplication...");
		SpringApplication.run(GulimallThirdPartyApplication.class, args);
		logger.info("GulimallThirdPartyApplication started successfully!");
	}

}
