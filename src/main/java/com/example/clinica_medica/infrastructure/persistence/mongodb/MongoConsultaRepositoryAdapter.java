package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Consulta;
import com.example.clinica_medica.domain.port.out.ConsultaRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MongoConsultaRepositoryAdapter implements ConsultaRepositoryPort {

  private final SpringDataConsultaRepository repository;

  public MongoConsultaRepositoryAdapter(SpringDataConsultaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Consulta save(Consulta consulta) {
    return repository.save(consulta);
  }

  @Override
  public List<Consulta> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Consulta> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
