package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.domain.port.out.MedicoRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MongoMedicoRepositoryAdapter implements MedicoRepositoryPort {

  private final SpringDataMedicoRepository repository;

  public MongoMedicoRepositoryAdapter(SpringDataMedicoRepository repository) {
    this.repository = repository;
  }

  @Override
  public Medico save(Medico medico) {
    return repository.save(medico);
  }

  @Override
  public List<Medico> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Medico> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
