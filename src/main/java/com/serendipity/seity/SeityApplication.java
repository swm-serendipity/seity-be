package com.serendipity.seity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.serendipity.seity.member", "com.serendipity.seity.forbiddenword" })
@EnableMongoRepositories(basePackages = { "com.serendipity.seity.prompt", "com.serendipity.seity.post",
		"com.serendipity.seity.detection", "com.serendipity.seity.calling", "com.serendipity.seity.dpr" })
public class SeityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeityApplication.class, args);
	}

}
