package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.repositories.MedicoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

  private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

  @Autowired private MedicoRepository medicoRepository;

  @Autowired private ValidationService validationService;

  public Medico incluirMedico(Medico medico) {
    logger.info(
        "Incluindo novo médico: {} (Especialidade: {})",
        medico.getNome(),
        medico.getEspecialidade());
    try {
      validationService.validarMedico(medico);
      Medico savedMedico = medicoRepository.save(medico);
      logger.info("Médico incluído com sucesso: ID {}", savedMedico.getId());
      return savedMedico;
    } catch (Exception e) {
      logger.error("Erro ao incluir médico {} - Erro: {}", medico.getNome(), e.getMessage());
      throw e;
    }
  }

  public void excluirMedico(String id) {
    logger.info("Excluindo médico com ID: {}", id);
    try {
      medicoRepository.deleteById(id);
      logger.info("Médico excluído com sucesso: ID {}", id);
    } catch (Exception e) {
      logger.error("Erro ao excluir médico ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Medico atualizarMedico(String id, Medico medico) {
    logger.info("Atualizando médico com ID: {}", id);
    try {
      Medico existingMedico = buscarMedicoPorId(id);
      if (existingMedico == null) {
        logger.error("Médico não encontrado para atualização: ID {}", id);
        throw new RuntimeException("Médico não encontrado");
      }
      medico.setId(id);
      validationService.validarMedico(medico);
      Medico updatedMedico = medicoRepository.save(medico);
      logger.info("Médico atualizado com sucesso: ID {}", id);
      return updatedMedico;
    } catch (Exception e) {
      logger.error("Erro ao atualizar médico ID {} - Erro: {}", id, e.getMessage());
      throw e;
    }
  }

  public Medico buscarMedicoPorId(String id) {
    logger.debug("Buscando médico por ID: {}", id);
    Optional<Medico> medico = medicoRepository.findById(id);
    if (medico.isPresent()) {
      logger.debug("Médico encontrado: ID {}", id);
    } else {
      logger.warn("Médico não encontrado: ID {}", id);
    }
    return medico.orElse(null);
  }

  public List<Medico> listarTodosMedicos() {
    logger.debug("Listando todos os médicos");
    List<Medico> medicos = medicoRepository.findAll();
    logger.info("Total de médicos encontrados: {}", medicos.size());
    return medicos;
  }
}
