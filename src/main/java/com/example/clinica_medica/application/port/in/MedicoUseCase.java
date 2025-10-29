package com.example.clinica_medica.application.port.in;

import com.example.clinica_medica.domain.model.Medico;
import java.util.List;

public interface MedicoUseCase {
  Medico incluirMedico(Medico medico);

  List<Medico> listarTodosMedicos();

  Medico buscarMedicoPorId(String id);

  Medico atualizarMedico(String id, Medico medico);

  void excluirMedico(String id);
}
