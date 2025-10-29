package com.example.clinica_medica.application.port.in;

import com.example.clinica_medica.domain.model.Paciente;
import java.util.List;

public interface PacienteUseCase {
  Paciente incluirPaciente(Paciente paciente);

  List<Paciente> listarTodosPacientes();

  Paciente buscarPacientePorCpf(String cpf);

  Paciente atualizarPaciente(String id, Paciente paciente);

  void excluirPaciente(String id);

  Paciente buscarPacientePorId(String id);
}
