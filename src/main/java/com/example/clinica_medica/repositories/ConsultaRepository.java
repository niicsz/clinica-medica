package com.example.clinica_medica.repositories;

import com.example.clinica_medica.entities.Consulta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends MongoRepository<Consulta, String> {}
