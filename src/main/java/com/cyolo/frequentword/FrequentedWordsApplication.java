package com.cyolo.frequentword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FrequentedWordsApplication {

	public static void main(String[] args) {

		SpringApplication.run(FrequentedWordsApplication.class, args);
	}

}
