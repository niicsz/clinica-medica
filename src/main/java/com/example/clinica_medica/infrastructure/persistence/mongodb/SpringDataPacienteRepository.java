package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Paciente;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPacienteRepository extends MongoRepository<Paciente, String> {
  Optional<Paciente> findByCpf(String cpf);
}
