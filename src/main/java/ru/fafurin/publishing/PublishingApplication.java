package ru.fafurin.publishing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PublishingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublishingApplication.class, args);
	}

}
