package com.example.clinica_medica.domain.port.out;

import com.example.clinica_medica.domain.model.Consulta;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepositoryPort {
  Consulta save(Consulta consulta);

  List<Consulta> findAll();

  Optional<Consulta> findById(String id);

  void deleteById(String id);
}
