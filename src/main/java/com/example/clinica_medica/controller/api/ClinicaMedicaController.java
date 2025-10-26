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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClinicaMedicaController implements ClinicaMedicaApi {

  private static final Logger logger = LoggerFactory.getLogger(ClinicaMedicaController.class);

  @Autowired private UsuarioService usuarioService;

  @Autowired private PacienteService pacienteService;

  @Autowired private MedicoService medicoService;

  @Autowired private ConsultaService consultaService;

  @Override
  public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
    logger.info("API - Requisição para cadastrar usuário: {}", usuario.getEmail());

    if (!CPFUtils.isCPFValido(usuario.getCpf())) {
      logger.warn("API - CPF inválido ao cadastrar usuário: {}", usuario.getCpf());
      return ResponseEntity.badRequest().body("CPF inválido.");
    }

    if (!EmailUtils.isEmailValido(usuario.getEmail())) {
      logger.warn("API - Email inválido ao cadastrar usuário: {}", usuario.getEmail());
      return ResponseEntity.badRequest().body("E-mail inválido.");
    }

    try {
      Usuario savedUsuario = usuarioService.incluirUsuario(usuario);
      logger.info("API - Usuário cadastrado com sucesso: ID {}", savedUsuario.getId());
      return ResponseEntity.ok(savedUsuario);
    } catch (Exception e) {
      logger.error("API - Erro ao cadastrar usuário: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public ResponseEntity<List<Usuario>> listarUsuarios() {
    logger.debug("API - Requisição para listar todos os usuários");
    return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
  }

  @Override
  public ResponseEntity<Usuario> atualizarUsuario(
      @PathVariable String id, @Valid @RequestBody Usuario usuario) {
    logger.info("API - Requisição para atualizar usuário ID: {}", id);
    return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
  }

  @Override
  public ResponseEntity<Void> excluirUsuario(@PathVariable String id) {
    logger.info("API - Requisição para excluir usuário ID: {}", id);
    usuarioService.excluirUsuario(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable String id) {
    logger.debug("API - Requisição para buscar usuário ID: {}", id);
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Paciente> cadastrarPaciente(@Valid @RequestBody Paciente paciente) {
    logger.info(
        "API - Requisição para cadastrar paciente: {} (CPF: {})",
        paciente.getNome(),
        paciente.getCpf());
    return ResponseEntity.ok(pacienteService.incluirPaciente(paciente));
  }

  @Override
  public ResponseEntity<List<Paciente>> listarPacientes() {
    logger.debug("API - Requisição para listar todos os pacientes");
    return ResponseEntity.ok(pacienteService.listarTodosPacientes());
  }

  @Override
  public ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable String cpf) {
    logger.debug("API - Requisição para buscar paciente por CPF: {}", cpf);
    Paciente paciente = pacienteService.buscarPacientePorCpf(cpf);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Paciente> atualizarPaciente(
      @PathVariable String id, @Valid @RequestBody Paciente paciente) {
    logger.info("API - Requisição para atualizar paciente ID: {}", id);
    return ResponseEntity.ok(pacienteService.atualizarPaciente(id, paciente));
  }

  @Override
  public ResponseEntity<Void> excluirPaciente(@PathVariable String id) {
    logger.info("API - Requisição para excluir paciente ID: {}", id);
    pacienteService.excluirPaciente(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable String id) {
    logger.debug("API - Requisição para buscar paciente ID: {}", id);
    Paciente paciente = pacienteService.buscarPacientePorId(id);
    return paciente != null ? ResponseEntity.ok(paciente) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Medico> cadastrarMedico(@Valid @RequestBody Medico medico) {
    logger.info(
        "API - Requisição para cadastrar médico: {} (Especialidade: {})",
        medico.getNome(),
        medico.getEspecialidade());
    return ResponseEntity.ok(medicoService.incluirMedico(medico));
  }

  @Override
  public ResponseEntity<List<Medico>> listarMedicos() {
    logger.debug("API - Requisição para listar todos os médicos");
    return ResponseEntity.ok(medicoService.listarTodosMedicos());
  }

  @Override
  public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable String id) {
    logger.debug("API - Requisição para buscar médico ID: {}", id);
    Medico medico = medicoService.buscarMedicoPorId(id);
    return medico != null ? ResponseEntity.ok(medico) : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<Medico> atualizarMedico(
      @PathVariable String id, @Valid @RequestBody Medico medico) {
    logger.info("API - Requisição para atualizar médico ID: {}", id);
    return ResponseEntity.ok(medicoService.atualizarMedico(id, medico));
  }

  @Override
  public ResponseEntity<Void> excluirMedico(@PathVariable String id) {
    logger.info("API - Requisição para excluir médico ID: {}", id);
    medicoService.excluirMedico(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Consulta> agendarConsulta(@Valid @RequestBody Consulta consulta) {
    logger.info("API - Requisição para agendar consulta");

    if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
      consulta.setPaciente(pacienteService.buscarPacientePorId(consulta.getPaciente().getId()));
    }
    if (consulta.getMedico() != null && consulta.getMedico().getId() != null) {
      consulta.setMedico(medicoService.buscarMedicoPorId(consulta.getMedico().getId()));
    }
    if (consulta.getPaciente() == null || consulta.getMedico() == null) {
      logger.error("API - Tentativa de agendar consulta sem paciente ou médico válidos");
      throw new IllegalArgumentException("Paciente e médico devem ser informados");
    }

    return ResponseEntity.ok(consultaService.agendarConsulta(consulta));
  }

  @Override
  public ResponseEntity<List<Consulta>> listarConsultas() {
    logger.debug("API - Requisição para listar todas as consultas");
    return ResponseEntity.ok(consultaService.listarTodasConsultas());
  }

  @Override
  public ResponseEntity<Consulta> atualizarConsulta(
      @PathVariable String id, @Valid @RequestBody Consulta consulta) {
    logger.info("API - Requisição para atualizar consulta ID: {}", id);

    if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
      consulta.setPaciente(pacienteService.buscarPacientePorId(consulta.getPaciente().getId()));
    }
    if (consulta.getMedico() != null && consulta.getMedico().getId() != null) {
      consulta.setMedico(medicoService.buscarMedicoPorId(consulta.getMedico().getId()));
    }
    if (consulta.getPaciente() == null || consulta.getMedico() == null) {
      logger.error("API - Tentativa de atualizar consulta sem paciente ou médico válidos");
      throw new IllegalArgumentException("Paciente e médico devem ser informados");
    }
    return ResponseEntity.ok(consultaService.atualizarConsulta(id, consulta));
  }

  @Override
  public ResponseEntity<Void> excluirConsulta(@PathVariable String id) {
    logger.info("API - Requisição para excluir consulta ID: {}", id);
    consultaService.excluirConsulta(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable String id) {
    logger.debug("API - Requisição para buscar consulta ID: {}", id);
    Consulta consulta = consultaService.buscarConsultaPorId(id);
    return consulta != null ? ResponseEntity.ok(consulta) : ResponseEntity.notFound().build();
  }
}
