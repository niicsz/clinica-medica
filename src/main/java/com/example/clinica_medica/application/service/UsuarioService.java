package com.example.clinica_medica.application.service;

import com.example.clinica_medica.application.port.in.UsuarioUseCase;
import com.example.clinica_medica.domain.model.Usuario;
import com.example.clinica_medica.domain.port.out.UsuarioRepositoryPort;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UsuarioUseCase {

  private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

  private final UsuarioRepositoryPort usuarioRepository;
  private final ValidationService validationService;
  private final PasswordEncoder passwordEncoder;

  public UsuarioService(
      UsuarioRepositoryPort usuarioRepository,
      ValidationService validationService,
      PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.validationService = validationService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Usuario incluirUsuario(Usuario usuario) {
    logger.info("Incluindo novo usuário: {} (Email: {})", usuario.getNome(), usuario.getEmail());
    validationService.validarUsuario(usuario);
    if (usuario.getRoles() != null) {
      usuario.setRoles(new HashSet<>(usuario.getRoles()));
    }
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    Usuario savedUsuario = usuarioRepository.save(usuario);
    logger.info("Usuário incluído com sucesso: ID {}", savedUsuario.getId());
    return savedUsuario;
  }

  @Override
  public List<Usuario> listarTodosUsuarios() {
    logger.debug("Listando todos os usuários");
    List<Usuario> usuarios = usuarioRepository.findAll();
    logger.info("Total de usuários encontrados: {}", usuarios.size());
    return usuarios;
  }

  @Override
  public void excluirUsuario(String id) {
    logger.info("Excluindo usuário com ID: {}", id);
    usuarioRepository.deleteById(id);
    logger.info("Usuário excluído com sucesso: ID {}", id);
  }

  @Override
  public Usuario atualizarUsuario(String id, Usuario usuario) {
    logger.info("Atualizando usuário com ID: {}", id);
    Usuario existingUsuario =
        usuarioRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  logger.error("Usuário não encontrado para atualização: ID {}", id);
                  return new RuntimeException("Usuário não encontrado");
                });

    usuario.setId(id);
    if (usuario.getRoles() != null) {
      usuario.setRoles(new HashSet<>(usuario.getRoles()));
    }
    validationService.validarUsuario(usuario);

    if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
      usuario.setSenha(existingUsuario.getSenha());
    } else if (!passwordEncoder.matches(usuario.getSenha(), existingUsuario.getSenha())) {
      usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }

    Usuario updatedUsuario = usuarioRepository.save(usuario);
    logger.info("Usuário atualizado com sucesso: ID {}", id);
    return updatedUsuario;
  }

  @Override
  public Usuario buscarUsuarioPorId(String id) {
    logger.debug("Buscando usuário por ID: {}", id);
    Usuario usuario = usuarioRepository.findById(id).orElse(null);
    if (usuario != null) {
      logger.debug("Usuário encontrado: ID {}", id);
    } else {
      logger.warn("Usuário não encontrado: ID {}", id);
    }
    return usuario;
  }

  @Override
  public Optional<Usuario> buscarPorEmail(String email) {
    logger.debug("Buscando usuário por email: {}", email);
    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
    if (usuario.isPresent()) {
      logger.debug("Usuário encontrado com email: {}", email);
    } else {
      logger.debug("Nenhum usuário encontrado com email: {}", email);
    }
    return usuario;
  }
}
