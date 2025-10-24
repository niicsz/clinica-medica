package com.example.clinica_medica.repositories;

import com.example.clinica_medica.entities.Paciente;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {
  Optional<Paciente> findByCpf(String cpf);
}
