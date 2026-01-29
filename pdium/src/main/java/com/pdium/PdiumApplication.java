package com.pdium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PdiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdiumApplication.class, args);
	}

}
