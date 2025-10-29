package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Medico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMedicoRepository extends MongoRepository<Medico, String> {}
