package com.example.clinica_medica.application.service;

import com.example.clinica_medica.application.port.in.ConsultaUseCase;
import com.example.clinica_medica.domain.model.Consulta;
import com.example.clinica_medica.domain.port.out.ConsultaRepositoryPort;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService implements ConsultaUseCase {

  private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

  private final ConsultaRepositoryPort consultaRepository;
  private final ValidationService validationService;

  public ConsultaService(
      ConsultaRepositoryPort consultaRepository, ValidationService validationService) {
    this.consultaRepository = consultaRepository;
    this.validationService = validationService;
  }

  @Override
  public Consulta agendarConsulta(Consulta consulta) {
    logger.info(
        "Agendando nova consulta para paciente ID: {} com médico ID: {}",
        consulta.getPaciente() != null ? consulta.getPaciente().getId() : "null",
        consulta.getMedico() != null ? consulta.getMedico().getId() : "null");
    validationService.validarConsulta(consulta);
    Consulta savedConsulta = consultaRepository.save(consulta);
    logger.info(
        "Consulta agendada com sucesso: ID {} para data/hora {}",
        savedConsulta.getId(),
        savedConsulta.getDataHora());
    return savedConsulta;
  }

  @Override
  public List<Consulta> listarTodasConsultas() {
    logger.debug("Listando todas as consultas");
    List<Consulta> consultas = consultaRepository.findAll();
    logger.info("Total de consultas encontradas: {}", consultas.size());
    return consultas;
  }

  @Override
  public void excluirConsulta(String id) {
    logger.info("Excluindo consulta com ID: {}", id);
    consultaRepository.deleteById(id);
    logger.info("Consulta excluída com sucesso: ID {}", id);
  }

  @Override
  public Consulta atualizarConsulta(String id, Consulta consulta) {
    logger.info("Atualizando consulta com ID: {}", id);
    consultaRepository
        .findById(id)
        .orElseThrow(
            () -> {
              logger.error("Consulta não encontrada para atualização: ID {}", id);
              return new RuntimeException("Consulta não encontrada");
            });

    consulta.setId(id);
    validationService.validarConsulta(consulta);
    Consulta updatedConsulta = consultaRepository.save(consulta);
    logger.info("Consulta atualizada com sucesso: ID {}", id);
    return updatedConsulta;
  }

  @Override
  public Consulta buscarConsultaPorId(String id) {
    logger.debug("Buscando consulta por ID: {}", id);
    return consultaRepository
        .findById(id)
        .map(
            consulta -> {
              logger.debug("Consulta encontrada: ID {}", id);
              return consulta;
            })
        .orElseGet(
            () -> {
              logger.warn("Consulta não encontrada: ID {}", id);
              return null;
            });
  }
}
