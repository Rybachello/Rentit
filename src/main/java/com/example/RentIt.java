package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RentIt {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RentIt.class, args);
	}
}
