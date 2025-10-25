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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClinicaMedicaController implements ClinicaMedicaApi {

  @Autowired private UsuarioService usuarioService;

  @Autowired private PacienteService pacienteService;

  @Autowired private MedicoService medicoService;

  @Autowired private ConsultaService consultaService;

  @PostMapping("/usuarios")
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
  @GetMapping("/usuarios")
  @Override
  public ResponseEntity<List<Usuario>> listarUsuarios() {
    return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
  }

  @PutMapping("/usuarios/{id}")
  @Override
  public ResponseEntity<Usuario> atualizarUsuario(
      @PathVariable String id, @Valid @RequestBody Usuario usuario) {
    return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
  }

  @DeleteMapping("/usuarios/{id}")
  @Override
  public ResponseEntity<Void> excluirUsuario(@PathVariable String id) {
    usuarioService.excluirUsuario(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/usuarios/{id}")
  @Override
  public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable String id) {
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
  }

  @PostMapping("/pacientes")
  @Override
  public ResponseEntity<Paciente> cadastrarPaciente(@Valid @RequestBody Paciente paciente) {
    return ResponseEntity.ok(pacienteService.incluirPaciente(paciente));
  }

  @GetMapping("/pacientes")
  @Override
  public ResponseEntity<List<Paciente>> listarPacientes() {
    return ResponseEntity.ok(pacienteService.listarTodosPacientes());
  }

  @GetMapping("/pacientes/cpf/{cpf}")
  @Override
  public ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable String cpf) {
    Paciente paciente = pacienteService.buscarPacientePorCpf(cpf);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @PutMapping("/pacientes/{id}")
  @Override
  public ResponseEntity<Paciente> atualizarPaciente(
      @PathVariable String id, @Valid @RequestBody Paciente paciente) {
    return ResponseEntity.ok(pacienteService.atualizarPaciente(id, paciente));
  }

  @DeleteMapping("/pacientes/{id}")
  @Override
  public ResponseEntity<Void> excluirPaciente(@PathVariable String id) {
    pacienteService.excluirPaciente(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/pacientes/{id}")
  @Override
  public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable String id) {
    Paciente paciente = pacienteService.buscarPacientePorId(id);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @PostMapping("/medicos")
  @Override
  public ResponseEntity<Medico> cadastrarMedico(@Valid @RequestBody Medico medico) {
    return ResponseEntity.ok(medicoService.incluirMedico(medico));
  }

  @GetMapping("/medicos")
  @Override
  public ResponseEntity<List<Medico>> listarMedicos() {
    return ResponseEntity.ok(medicoService.listarTodosMedicos());
  }

  @GetMapping("/medicos/{id}")
  @Override
  public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable String id) {
    Medico medico = medicoService.buscarMedicoPorId(id);
    return medico != null ? ResponseEntity.ok(medico) : ResponseEntity.notFound().build();
  }

  @PutMapping("/medicos/{id}")
  @Override
  public ResponseEntity<Medico> atualizarMedico(
      @PathVariable String id, @Valid @RequestBody Medico medico) {
    return ResponseEntity.ok(medicoService.atualizarMedico(id, medico));
  }

  @DeleteMapping("/medicos/{id}")
  @Override
  public ResponseEntity<Void> excluirMedico(@PathVariable String id) {
    medicoService.excluirMedico(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/consultas")
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

  @GetMapping("/consultas")
  @Override
  public ResponseEntity<List<Consulta>> listarConsultas() {
    return ResponseEntity.ok(consultaService.listarTodasConsultas());
  }

  @PutMapping("/consultas/{id}")
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

  @DeleteMapping("/consultas/{id}")
  @Override
  public ResponseEntity<Void> excluirConsulta(@PathVariable String id) {
    consultaService.excluirConsulta(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/consultas/{id}")
  @Override
  public ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable String id) {
    Consulta consulta = consultaService.buscarConsultaPorId(id);
    return consulta != null ? ResponseEntity.ok(consulta) : ResponseEntity.notFound().build();
  }
}
