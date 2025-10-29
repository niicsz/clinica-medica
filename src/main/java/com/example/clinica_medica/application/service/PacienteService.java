package com.example.clinica_medica.application.service;

import com.example.clinica_medica.application.port.in.PacienteUseCase;
import com.example.clinica_medica.domain.model.Paciente;
import com.example.clinica_medica.domain.port.out.PacienteRepositoryPort;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PacienteService implements PacienteUseCase {

  private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

  private final PacienteRepositoryPort pacienteRepository;
  private final ValidationService validationService;

  public PacienteService(
      PacienteRepositoryPort pacienteRepository, ValidationService validationService) {
    this.pacienteRepository = pacienteRepository;
    this.validationService = validationService;
  }

  @Override
  public Paciente incluirPaciente(Paciente paciente) {
    logger.info("Incluindo novo paciente: {} (CPF: {})", paciente.getNome(), paciente.getCpf());
    validationService.validarPaciente(paciente);
    Paciente savedPaciente = pacienteRepository.save(paciente);
    logger.info("Paciente incluído com sucesso: ID {}", savedPaciente.getId());
    return savedPaciente;
  }

  @Override
  public Paciente buscarPacientePorCpf(String cpf) {
    logger.debug("Buscando paciente por CPF: {}", cpf);
    return pacienteRepository
        .findByCpf(cpf)
        .map(
            paciente -> {
              logger.debug("Paciente encontrado com CPF: {}", cpf);
              return paciente;
            })
        .orElseGet(
            () -> {
              logger.warn("Paciente não encontrado com CPF: {}", cpf);
              return null;
            });
  }

  @Override
  public List<Paciente> listarTodosPacientes() {
    logger.debug("Listando todos os pacientes");
    List<Paciente> pacientes = pacienteRepository.findAll();
    logger.info("Total de pacientes encontrados: {}", pacientes.size());
    return pacientes;
  }

  @Override
  public void excluirPaciente(String id) {
    logger.info("Excluindo paciente com ID: {}", id);
    pacienteRepository.deleteById(id);
    logger.info("Paciente excluído com sucesso: ID {}", id);
  }

  @Override
  public Paciente atualizarPaciente(String id, Paciente paciente) {
    logger.info("Atualizando paciente com ID: {}", id);
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
  }

  @Override
  public Paciente buscarPacientePorId(String id) {
    logger.debug("Buscando paciente por ID: {}", id);
    return pacienteRepository
        .findById(id)
        .map(
            paciente -> {
              logger.debug("Paciente encontrado: ID {}", id);
              return paciente;
            })
        .orElseGet(
            () -> {
              logger.warn("Paciente não encontrado: ID {}", id);
              return null;
            });
  }
}
