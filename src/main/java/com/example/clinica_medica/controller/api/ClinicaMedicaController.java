package com.example.clinica_medica.controller.api;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.generated.api.ClinicaMedicaApi;
import com.example.clinica_medica.services.ConsultaService;
import com.example.clinica_medica.services.MedicoService;
import com.example.clinica_medica.services.PacienteService;
import com.example.clinica_medica.services.UsuarioService;
import com.example.clinica_medica.utils.CPFUtils;
import com.example.clinica_medica.utils.EmailUtils;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClinicaMedicaController implements ClinicaMedicaApi {

  @Autowired private UsuarioService usuarioService;

  @Autowired private PacienteService pacienteService;

  @Autowired private MedicoService medicoService;

  @Autowired private ConsultaService consultaService;

  @Override
  public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
    if (!CPFUtils.isCPFValido(usuario.getCpf())) {
      return ResponseEntity.badRequest().body("CPF inválido.");
    }

    if (!EmailUtils.isEmailValido(usuario.getEmail())) {
      return ResponseEntity.badRequest().body("E-mail inválido.");
    }

    return ResponseEntity.ok(usuarioService.incluirUsuario(usuario));
  }
  @Override
  public ResponseEntity<List<Usuario>> listarUsuarios() {
    return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
  }

  @Override
  public ResponseEntity<Usuario> atualizarUsuario(
      @PathVariable String id, @Valid @RequestBody Usuario usuario) {
    return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
  }

  @Override
  public ResponseEntity<Void> excluirUsuario(@PathVariable String id) {
    usuarioService.excluirUsuario(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable String id) {
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Paciente> cadastrarPaciente(@Valid @RequestBody Paciente paciente) {
    return ResponseEntity.ok(pacienteService.incluirPaciente(paciente));
  }

  @Override
  public ResponseEntity<List<Paciente>> listarPacientes() {
    return ResponseEntity.ok(pacienteService.listarTodosPacientes());
  }

  @Override
  public ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable String cpf) {
    Paciente paciente = pacienteService.buscarPacientePorCpf(cpf);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Paciente> atualizarPaciente(
      @PathVariable String id, @Valid @RequestBody Paciente paciente) {
    return ResponseEntity.ok(pacienteService.atualizarPaciente(id, paciente));
  }

  @Override
  public ResponseEntity<Void> excluirPaciente(@PathVariable String id) {
    pacienteService.excluirPaciente(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable String id) {
    Paciente paciente = pacienteService.buscarPacientePorId(id);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Medico> cadastrarMedico(@Valid @RequestBody Medico medico) {
    return ResponseEntity.ok(medicoService.incluirMedico(medico));
  }

  @Override
  public ResponseEntity<List<Medico>> listarMedicos() {
    return ResponseEntity.ok(medicoService.listarTodosMedicos());
  }

  @Override
  public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable String id) {
    Medico medico = medicoService.buscarMedicoPorId(id);
    return medico != null ? ResponseEntity.ok(medico) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Medico> atualizarMedico(
      @PathVariable String id, @Valid @RequestBody Medico medico) {
    return ResponseEntity.ok(medicoService.atualizarMedico(id, medico));
  }

  @Override
  public ResponseEntity<Void> excluirMedico(@PathVariable String id) {
    medicoService.excluirMedico(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Consulta> agendarConsulta(@Valid @RequestBody Consulta consulta) {
    if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
      consulta.setPaciente(pacienteService.buscarPacientePorId(consulta.getPaciente().getId()));
    }
    if (consulta.getMedico() != null && consulta.getMedico().getId() != null) {
      consulta.setMedico(medicoService.buscarMedicoPorId(consulta.getMedico().getId()));
    }
    if (consulta.getPaciente() == null || consulta.getMedico() == null) {
      throw new IllegalArgumentException("Paciente e médico devem ser informados");
    }
    return ResponseEntity.ok(consultaService.agendarConsulta(consulta));
  }

  @Override
  public ResponseEntity<List<Consulta>> listarConsultas() {
    return ResponseEntity.ok(consultaService.listarTodasConsultas());
  }

  @Override
  public ResponseEntity<Consulta> atualizarConsulta(
      @PathVariable String id, @Valid @RequestBody Consulta consulta) {
    if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
      consulta.setPaciente(pacienteService.buscarPacientePorId(consulta.getPaciente().getId()));
    }
    if (consulta.getMedico() != null && consulta.getMedico().getId() != null) {
      consulta.setMedico(medicoService.buscarMedicoPorId(consulta.getMedico().getId()));
    }
    if (consulta.getPaciente() == null || consulta.getMedico() == null) {
      throw new IllegalArgumentException("Paciente e médico devem ser informados");
    }
    return ResponseEntity.ok(consultaService.atualizarConsulta(id, consulta));
  }

  @Override
  public ResponseEntity<Void> excluirConsulta(@PathVariable String id) {
    consultaService.excluirConsulta(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable String id) {
    Consulta consulta = consultaService.buscarConsultaPorId(id);
    return consulta != null ? ResponseEntity.ok(consulta) : ResponseEntity.notFound().build();
  }
}
