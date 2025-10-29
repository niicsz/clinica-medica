package com.example.clinica_medica.application.service;

import com.example.clinica_medica.application.port.in.MedicoUseCase;
import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.domain.port.out.MedicoRepositoryPort;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MedicoService implements MedicoUseCase {

  private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

  private final MedicoRepositoryPort medicoRepository;
  private final ValidationService validationService;

  public MedicoService(MedicoRepositoryPort medicoRepository, ValidationService validationService) {
    this.medicoRepository = medicoRepository;
    this.validationService = validationService;
  }

  @Override
  public Medico incluirMedico(Medico medico) {
    logger.info(
        "Incluindo novo médico: {} (Especialidade: {})",
        medico.getNome(),
        medico.getEspecialidade());
    validationService.validarMedico(medico);
    Medico savedMedico = medicoRepository.save(medico);
    logger.info("Médico incluído com sucesso: ID {}", savedMedico.getId());
    return savedMedico;
  }

  @Override
  public void excluirMedico(String id) {
    logger.info("Excluindo médico com ID: {}", id);
    medicoRepository.deleteById(id);
    logger.info("Médico excluído com sucesso: ID {}", id);
  }

  @Override
  public Medico atualizarMedico(String id, Medico medico) {
    logger.info("Atualizando médico com ID: {}", id);
    medicoRepository
        .findById(id)
        .orElseThrow(
            () -> {
              logger.error("Médico não encontrado para atualização: ID {}", id);
              return new RuntimeException("Médico não encontrado");
            });

    medico.setId(id);
    validationService.validarMedico(medico);
    Medico updatedMedico = medicoRepository.save(medico);
    logger.info("Médico atualizado com sucesso: ID {}", id);
    return updatedMedico;
  }

  @Override
  public Medico buscarMedicoPorId(String id) {
    logger.debug("Buscando médico por ID: {}", id);
    return medicoRepository
        .findById(id)
        .map(
            medico -> {
              logger.debug("Médico encontrado: ID {}", id);
              return medico;
            })
        .orElseGet(
            () -> {
              logger.warn("Médico não encontrado: ID {}", id);
              return null;
            });
  }

  @Override
  public List<Medico> listarTodosMedicos() {
    logger.debug("Listando todos os médicos");
    List<Medico> medicos = medicoRepository.findAll();
    logger.info("Total de médicos encontrados: {}", medicos.size());
    return medicos;
  }
}
