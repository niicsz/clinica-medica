package com.example.clinica_medica.generated.api;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Generated(value = "swagger-codegen", date = "2024-10-24")
public interface ClinicaMedicaApi {

  ResponseEntity<?> cadastrarUsuario(@Valid Usuario usuario);

  ResponseEntity<List<Usuario>> listarUsuarios();

  ResponseEntity<Usuario> atualizarUsuario(String id, @Valid Usuario usuario);

  ResponseEntity<Void> excluirUsuario(String id);

  ResponseEntity<Usuario> buscarUsuarioPorId(String id);

  ResponseEntity<Paciente> cadastrarPaciente(@Valid Paciente paciente);

  ResponseEntity<List<Paciente>> listarPacientes();

  ResponseEntity<Paciente> buscarPacientePorCpf(String cpf);

  ResponseEntity<Paciente> atualizarPaciente(String id, @Valid Paciente paciente);

  ResponseEntity<Void> excluirPaciente(String id);

  ResponseEntity<Paciente> buscarPacientePorId(String id);

  ResponseEntity<Medico> cadastrarMedico(@Valid Medico medico);

  ResponseEntity<List<Medico>> listarMedicos();

  ResponseEntity<Medico> buscarMedicoPorId(String id);

  ResponseEntity<Medico> atualizarMedico(String id, @Valid Medico medico);

  ResponseEntity<Void> excluirMedico(String id);

  ResponseEntity<Consulta> agendarConsulta(@Valid Consulta consulta);

  ResponseEntity<List<Consulta>> listarConsultas();

  ResponseEntity<Consulta> atualizarConsulta(String id, @Valid Consulta consulta);

  ResponseEntity<Void> excluirConsulta(String id);

  ResponseEntity<Consulta> buscarConsultaPorId(String id);
}
