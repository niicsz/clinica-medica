package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Consulta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataConsultaRepository extends MongoRepository<Consulta, String> {}
