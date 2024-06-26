package com.maticz.BirthdaysDWH;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class
BirthdaysDwhApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirthdaysDwhApplication.class, args);
	}

}
