package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.repositories.PacienteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
  @Autowired private PacienteRepository pacienteRepository;

  @Autowired private ValidationService validationService;

  public Paciente incluirPaciente(Paciente paciente) {
    validationService.validarPaciente(paciente);
    return pacienteRepository.save(paciente);
  }

  public Paciente buscarPacientePorCpf(String cpf) {
    return pacienteRepository.findByCpf(cpf).orElse(null);
  }

  public List<Paciente> listarTodosPacientes() {
    return pacienteRepository.findAll();
  }

  public void excluirPaciente(String id) {
    pacienteRepository.deleteById(id);
  }

  public Paciente atualizarPaciente(String id, Paciente paciente) {
    Paciente existingPaciente =
        pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    paciente.setId(id);
    validationService.validarPaciente(paciente);
    return pacienteRepository.save(paciente);
  }

  public Paciente buscarPacientePorId(String id) {
    return pacienteRepository.findById(id).orElse(null);
  }

  public Optional<Paciente> buscarOptionalPorCpf(String cpf) {
    return pacienteRepository.findByCpf(cpf);
  }
}
