package com.serendipity.seity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SeityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeityApplication.class, args);
	}

}
