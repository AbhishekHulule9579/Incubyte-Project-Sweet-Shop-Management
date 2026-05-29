package com.sweetcorner.incubyte_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IncubyteBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(IncubyteBackendApplication.class, args);
	}

}
