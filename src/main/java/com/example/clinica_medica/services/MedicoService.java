package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.repositories.MedicoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {
  @Autowired private MedicoRepository medicoRepository;

  @Autowired private ValidationService validationService;

  public Medico incluirMedico(Medico medico) {
    validationService.validarMedico(medico);
    return medicoRepository.save(medico);
  }

  public void excluirMedico(String id) {
    medicoRepository.deleteById(id);
  }

  public Medico atualizarMedico(String id, Medico medico) {
    Medico existingMedico = buscarMedicoPorId(id);
    if (existingMedico == null) {
      throw new RuntimeException("Médico não encontrado");
    }
    medico.setId(id);
    validationService.validarMedico(medico);
    return medicoRepository.save(medico);
  }

  public Medico buscarMedicoPorId(String id) {
    Optional<Medico> medico = medicoRepository.findById(id);
    return medico.orElse(null);
  }

  public List<Medico> listarTodosMedicos() {
    return medicoRepository.findAll();
  }
}
