package com.example.clinica_medica.application.service;

import com.example.clinica_medica.domain.model.Consulta;
import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.domain.model.Paciente;
import com.example.clinica_medica.domain.model.Usuario;
import com.example.clinica_medica.domain.port.out.ConsultaRepositoryPort;
import com.example.clinica_medica.domain.port.out.MedicoRepositoryPort;
import com.example.clinica_medica.domain.port.out.PacienteRepositoryPort;
import com.example.clinica_medica.domain.port.out.UsuarioRepositoryPort;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private final PacienteRepositoryPort pacienteRepository;
  private final MedicoRepositoryPort medicoRepository;
  private final ConsultaRepositoryPort consultaRepository;
  private final UsuarioRepositoryPort usuarioRepository;

  private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

  public ValidationService(
      PacienteRepositoryPort pacienteRepository,
      MedicoRepositoryPort medicoRepository,
      ConsultaRepositoryPort consultaRepository,
      UsuarioRepositoryPort usuarioRepository) {
    this.pacienteRepository = pacienteRepository;
    this.medicoRepository = medicoRepository;
    this.consultaRepository = consultaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  public void validarPaciente(Paciente paciente) {
    if (paciente.getCpf() == null || !CPF_PATTERN.matcher(paciente.getCpf()).matches()) {
      throw new IllegalArgumentException("CPF deve conter 11 dígitos numéricos");
    }

    if (paciente.getEmail() == null || !EMAIL_PATTERN.matcher(paciente.getEmail()).matches()) {
      throw new IllegalArgumentException("E-mail inválido");
    }

    if (paciente.getNome() == null || paciente.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }

    if (paciente.getIdade() == null || paciente.getIdade() < 0) {
      throw new IllegalArgumentException("Idade deve ser um número positivo");
    }

    Paciente existingPaciente = pacienteRepository.findByCpf(paciente.getCpf()).orElse(null);
    if (existingPaciente != null && !existingPaciente.getId().equals(paciente.getId())) {
      throw new IllegalArgumentException("CPF já cadastrado para outro paciente");
    }
  }

  public void validarMedico(Medico medico) {
    if (medico.getNome() == null || medico.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }

    if (medico.getEspecialidade() == null || medico.getEspecialidade().trim().isEmpty()) {
      throw new IllegalArgumentException("Especialidade é obrigatória");
    }

    medicoRepository
        .findById(medico.getId())
        .ifPresent(
            existing -> {
              if (!existing.getEspecialidade().equalsIgnoreCase(medico.getEspecialidade())) {
                throw new IllegalArgumentException("Especialidade não pode ser alterada");
              }
            });
  }

  public void validarUsuario(Usuario usuario) {
    if (usuario.getCpf() == null || !CPF_PATTERN.matcher(usuario.getCpf()).matches()) {
      throw new IllegalArgumentException("CPF deve conter 11 dígitos numéricos");
    }

    if (usuario.getEmail() == null || !EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
      throw new IllegalArgumentException("E-mail inválido");
    }

    if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }

    if (usuario.getIdade() == null || usuario.getIdade() < 0) {
      throw new IllegalArgumentException("Idade deve ser um número positivo");
    }

    if (usuario.getId() == null) {
      if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
        throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
      }
    } else if (usuario.getSenha() != null
        && !usuario.getSenha().isBlank()
        && usuario.getSenha().length() < 6) {
      throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
    }

    if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
      throw new IllegalArgumentException("Usuário deve possuir ao menos uma permissão");
    }

    Usuario existingUsuario = usuarioRepository.findByCpf(usuario.getCpf()).orElse(null);
    if (existingUsuario != null && !existingUsuario.getId().equals(usuario.getId())) {
      throw new IllegalArgumentException("CPF já cadastrado para outro usuário");
    }

    Usuario usuarioPorEmail = usuarioRepository.findByEmail(usuario.getEmail()).orElse(null);
    if (usuarioPorEmail != null && !usuarioPorEmail.getId().equals(usuario.getId())) {
      throw new IllegalArgumentException("E-mail já cadastrado para outro usuário");
    }
  }

  public void validarConsulta(Consulta consulta) {
    if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
      throw new IllegalArgumentException("Paciente é obrigatório");
    }

    if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
      throw new IllegalArgumentException("Médico é obrigatório");
    }

    if (consulta.getDataHora() == null) {
      throw new IllegalArgumentException("Data e hora são obrigatórias");
    }

    if (consulta.getDataHora().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("A data e hora da consulta deve ser futura");
    }

    if (consulta.getTipoConsulta() == null || consulta.getTipoConsulta().trim().isEmpty()) {
      throw new IllegalArgumentException("Tipo de consulta é obrigatório");
    }

    boolean medicoOcupado =
        consultaRepository.findAll().stream()
            .anyMatch(
                c ->
                    c.getMedico().getId().equals(consulta.getMedico().getId())
                        && c.getDataHora().equals(consulta.getDataHora())
                        && (consulta.getId() == null || !c.getId().equals(consulta.getId())));

    if (medicoOcupado) {
      throw new IllegalArgumentException("Médico já possui consulta agendada para este horário");
    }
  }
}
