package com.sweetcorner.incubyte_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Sweet Corner Backend Application.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class IncubyteBackendApplication {

	/**
	 * The main method to start the Spring Boot application.
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(IncubyteBackendApplication.class, args);
	}

}
