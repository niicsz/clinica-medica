package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.exceptions.ResourceNotFoundException;
import com.example.clinica_medica.repositories.UsuarioRepository;
import com.example.clinica_medica.utils.CPFUtils;
import com.example.clinica_medica.utils.EmailUtils;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final ValidationService validationService;

  public UsuarioService(UsuarioRepository usuarioRepository, ValidationService validationService) {
    this.usuarioRepository = usuarioRepository;
    this.validationService = validationService;
  }

  @Transactional
  public Usuario incluirUsuario(Usuario usuario) {
    validarCPFEEmail(usuario);
    validationService.validarUsuario(usuario);
    return usuarioRepository.save(usuario);
  }

  @Transactional(readOnly = true)
  public List<Usuario> listarTodosUsuarios() {
    return usuarioRepository.findAll();
  }

  @Transactional
  public void excluirUsuario(Long id) {
    usuarioRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    usuarioRepository.deleteById(id);
  }

  @Transactional
  public Usuario atualizarUsuario(Long id, Usuario usuario) {
    usuarioRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    validarCPFEEmail(usuario);
    usuario.setId(id.intValue());
    validationService.validarUsuario(usuario);
    return usuarioRepository.save(usuario);
  }

  @Transactional(readOnly = true)
  public Usuario buscarUsuarioPorId(Long id) {
    return usuarioRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
  }

  private void validarCPFEEmail(Usuario usuario) {
    if (!CPFUtils.isCPFValido(usuario.getCpf())) {
      throw new IllegalArgumentException("CPF inválido");
    }
    if (!EmailUtils.isEmailValido(usuario.getEmail())) {
      throw new IllegalArgumentException("E-mail inválido");
    }
  }
}
