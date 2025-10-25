package com.example.clinica_medica.config;

import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoCollectionInitializer {

  private static final List<String> COLLECTIONS =
      List.of("usuarios", "pacientes", "medicos", "consultas");

  @Bean
  public ApplicationRunner ensureCollections(MongoTemplate mongoTemplate) {
    return args -> {
      for (String collection : COLLECTIONS) {
        if (!mongoTemplate.collectionExists(collection)) {
          mongoTemplate.createCollection(collection);
        }
      }
    };
  }
}
