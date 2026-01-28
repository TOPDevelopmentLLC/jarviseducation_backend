package com.top.jarvised;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JarvisEdBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JarvisEdBackendApplication.class, args);
	}

}
