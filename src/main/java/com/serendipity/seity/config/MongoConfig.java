package com.serendipity.seity.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
        return new MongoTemplate(factory);
    }
}
