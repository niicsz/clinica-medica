package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.repositories.PacienteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

  private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

  @Autowired private PacienteRepository pacienteRepository;

  @Autowired private ValidationService validationService;

  public Paciente incluirPaciente(Paciente paciente) {
    logger.info("Incluindo novo paciente: {} (CPF: {})", paciente.getNome(), paciente.getCpf());
    try {
      validationService.validarPaciente(paciente);
      Paciente savedPaciente = pacienteRepository.save(paciente);
      logger.info("Paciente incluído com sucesso: ID {}", savedPaciente.getId());
      return savedPaciente;
    } catch (Exception e) {
      logger.error("Erro ao incluir paciente {} - Erro: {}", paciente.getCpf(), e.getMessage());
      throw e;
    }
  }

  public Paciente buscarPacientePorCpf(String cpf) {
    logger.debug("Buscando paciente por CPF: {}", cpf);
    Paciente paciente = pacienteRepository.findByCpf(cpf).orElse(null);
    if (paciente != null) {
      logger.debug("Paciente encontrado com CPF: {}", cpf);
    } else {
      logger.warn("Paciente não encontrado com CPF: {}", cpf);
    }
    return paciente;
  }

  public List<Paciente> listarTodosPacientes() {
    logger.debug("Listando todos os pacientes");
    List<Paciente> pacientes = pacienteRepository.findAll();
    logger.info("Total de pacientes encontrados: {}", pacientes.size());
    return pacientes;
  }

  public void excluirPaciente(String id) {
    logger.info("Excluindo paciente com ID: {}", id);
    try {
      pacienteRepository.deleteById(id);
      logger.info("Paciente excluído com sucesso: ID {}", id);
    } catch (Exception e) {
      logger.error("Erro ao excluir paciente ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Paciente atualizarPaciente(String id, Paciente paciente) {
    logger.info("Atualizando paciente com ID: {}", id);
    try {
      Paciente existingPaciente =
          pacienteRepository
              .findById(id)
              .orElseThrow(
                  () -> {
                    logger.error("Paciente não encontrado para atualização: ID {}", id);
                    return new RuntimeException("Paciente não encontrado");
                  });
      paciente.setId(id);
      validationService.validarPaciente(paciente);
      Paciente updatedPaciente = pacienteRepository.save(paciente);
      logger.info("Paciente atualizado com sucesso: ID {}", id);
      return updatedPaciente;
    } catch (Exception e) {
      logger.error("Erro ao atualizar paciente ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Paciente buscarPacientePorId(String id) {
    logger.debug("Buscando paciente por ID: {}", id);
    Paciente paciente = pacienteRepository.findById(id).orElse(null);
    if (paciente != null) {
      logger.debug("Paciente encontrado: ID {}", id);
    } else {
      logger.warn("Paciente não encontrado: ID {}", id);
    }
    return paciente;
  }

  public Optional<Paciente> buscarOptionalPorCpf(String cpf) {
    logger.debug("Buscando paciente (Optional) por CPF: {}", cpf);
    return pacienteRepository.findByCpf(cpf);
  }
}
