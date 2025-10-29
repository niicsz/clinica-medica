package com.example.clinica_medica.domain.port.out;

import com.example.clinica_medica.domain.model.Paciente;
import java.util.List;
import java.util.Optional;

public interface PacienteRepositoryPort {
  Paciente save(Paciente paciente);

  List<Paciente> findAll();

  Optional<Paciente> findByCpf(String cpf);

  Optional<Paciente> findById(String id);

  void deleteById(String id);
}
