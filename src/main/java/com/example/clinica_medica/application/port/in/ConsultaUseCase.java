package com.example.clinica_medica.application.port.in;

import com.example.clinica_medica.domain.model.Consulta;
import java.util.List;

public interface ConsultaUseCase {
  Consulta agendarConsulta(Consulta consulta);

  List<Consulta> listarTodasConsultas();

  Consulta atualizarConsulta(String id, Consulta consulta);

  void excluirConsulta(String id);

  Consulta buscarConsultaPorId(String id);
}
