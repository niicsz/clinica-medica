package com.example.clinica_medica.repositories;

import com.example.clinica_medica.entities.Medico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends MongoRepository<Medico, String> {}
