package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Paciente;
import com.example.clinica_medica.domain.port.out.PacienteRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MongoPacienteRepositoryAdapter implements PacienteRepositoryPort {

  private final SpringDataPacienteRepository repository;

  public MongoPacienteRepositoryAdapter(SpringDataPacienteRepository repository) {
    this.repository = repository;
  }

  @Override
  public Paciente save(Paciente paciente) {
    return repository.save(paciente);
  }

  @Override
  public List<Paciente> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Paciente> findByCpf(String cpf) {
    return repository.findByCpf(cpf);
  }

  @Override
  public Optional<Paciente> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
