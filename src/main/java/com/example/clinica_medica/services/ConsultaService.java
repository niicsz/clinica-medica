package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.repositories.ConsultaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {
  private final ConsultaRepository consultaRepository;
  private final ValidationService validationService;

  public ConsultaService(
      ConsultaRepository consultaRepository, ValidationService validationService) {
    this.consultaRepository = consultaRepository;
    this.validationService = validationService;
  }

  @Transactional
  public Consulta agendarConsulta(Consulta consulta) {
    validationService.validarConsulta(consulta);
    return consultaRepository.save(consulta);
  }

  @Transactional(readOnly = true)
  public List<Consulta> listarTodasConsultas() {
    return consultaRepository.findAll();
  }

  @Transactional
  public void excluirConsulta(Long id) {
    consultaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    consultaRepository.deleteById(id);
  }

  @Transactional
  public Consulta atualizarConsulta(Long id, Consulta consulta) {
    consultaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    consulta.setId(id);
    validationService.validarConsulta(consulta);
    return consultaRepository.save(consulta);
  }

  @Transactional(readOnly = true)
  public Consulta buscarConsultaPorId(Long id) {
    return consultaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
  }
}
