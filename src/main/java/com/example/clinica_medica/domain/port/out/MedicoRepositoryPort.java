package com.example.clinica_medica.domain.port.out;

import com.example.clinica_medica.domain.model.Medico;
import java.util.List;
import java.util.Optional;

public interface MedicoRepositoryPort {
  Medico save(Medico medico);

  List<Medico> findAll();

  Optional<Medico> findById(String id);

  void deleteById(String id);
}
