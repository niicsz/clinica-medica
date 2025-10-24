package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.repositories.ConsultaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {
  @Autowired private ConsultaRepository consultaRepository;

  @Autowired private ValidationService validationService;

  public Consulta agendarConsulta(Consulta consulta) {
    validationService.validarConsulta(consulta);
    return consultaRepository.save(consulta);
  }

  public List<Consulta> listarTodasConsultas() {
    return consultaRepository.findAll();
  }

  public void excluirConsulta(String id) {
    consultaRepository.deleteById(id);
  }

  public Consulta atualizarConsulta(String id, Consulta consulta) {
    consultaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    consulta.setId(id);
    validationService.validarConsulta(consulta);
    return consultaRepository.save(consulta);
  }

  public Consulta buscarConsultaPorId(String id) {
    return consultaRepository.findById(id).orElse(null);
  }
}
