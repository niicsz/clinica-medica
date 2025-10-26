package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.repositories.ConsultaRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

  private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

  @Autowired private ConsultaRepository consultaRepository;

  @Autowired private ValidationService validationService;

  public Consulta agendarConsulta(Consulta consulta) {
    logger.info(
        "Agendando nova consulta para paciente ID: {} com médico ID: {}",
        consulta.getPaciente() != null ? consulta.getPaciente().getId() : "null",
        consulta.getMedico() != null ? consulta.getMedico().getId() : "null");
    try {
      validationService.validarConsulta(consulta);
      Consulta savedConsulta = consultaRepository.save(consulta);
      logger.info(
          "Consulta agendada com sucesso: ID {} para data/hora {}",
          savedConsulta.getId(),
          savedConsulta.getDataHora());
      return savedConsulta;
    } catch (Exception e) {
      logger.error("Erro ao agendar consulta - Erro: {}", e.getMessage());
      throw e;
    }
  }

  public List<Consulta> listarTodasConsultas() {
    logger.debug("Listando todas as consultas");
    List<Consulta> consultas = consultaRepository.findAll();
    logger.info("Total de consultas encontradas: {}", consultas.size());
    return consultas;
  }

  public void excluirConsulta(String id) {
    logger.info("Excluindo consulta com ID: {}", id);
    try {
      consultaRepository.deleteById(id);
      logger.info("Consulta excluída com sucesso: ID {}", id);
    } catch (Exception e) {
      logger.error("Erro ao excluir consulta ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Consulta atualizarConsulta(String id, Consulta consulta) {
    logger.info("Atualizando consulta com ID: {}", id);
    try {
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
    } catch (Exception e) {
      logger.error("Erro ao atualizar consulta ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Consulta buscarConsultaPorId(String id) {
    logger.debug("Buscando consulta por ID: {}", id);
    Consulta consulta = consultaRepository.findById(id).orElse(null);
    if (consulta != null) {
      logger.debug("Consulta encontrada: ID {}", id);
    } else {
      logger.warn("Consulta não encontrada: ID {}", id);
    }
    return consulta;
  }
}
