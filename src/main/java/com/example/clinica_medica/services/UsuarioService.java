package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired private UsuarioRepository usuarioRepository;

  @Autowired private ValidationService validationService;

  @Autowired private PasswordEncoder passwordEncoder;

  public Usuario incluirUsuario(Usuario usuario) {
    validationService.validarUsuario(usuario);
    if (usuario.getRoles() != null) {
      usuario.setRoles(new java.util.HashSet<>(usuario.getRoles()));
    }
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    return usuarioRepository.save(usuario);
  }

  public List<Usuario> listarTodosUsuarios() {
    return usuarioRepository.findAll();
  }

  public void excluirUsuario(String id) {
    usuarioRepository.deleteById(id);
  }

  public Usuario atualizarUsuario(String id, Usuario usuario) {
    Usuario existingUsuario =
        usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    usuario.setId(id);
    if (usuario.getRoles() != null) {
      usuario.setRoles(new java.util.HashSet<>(usuario.getRoles()));
    }
    validationService.validarUsuario(usuario);

    if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
      usuario.setSenha(existingUsuario.getSenha());
    } else if (!passwordEncoder.matches(usuario.getSenha(), existingUsuario.getSenha())) {
      usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }

    return usuarioRepository.save(usuario);
  }

  public Usuario buscarUsuarioPorId(String id) {
    return usuarioRepository.findById(id).orElse(null);
  }

  public Optional<Usuario> buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }
}
