package com.example.clinica_medica.security;

import com.example.clinica_medica.domain.port.out.UsuarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final UsuarioRepositoryPort usuarioRepository;

  public CustomUserDetailsService(UsuarioRepositoryPort usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.debug("Carregando detalhes do usuário: {}", username);
    return usuarioRepository
        .findByEmail(username)
        .map(usuario -> {
          logger.debug("Usuário encontrado: {} com roles: {}", username, usuario.getRoles());
          return new UsuarioDetails(usuario);
        })
        .orElseThrow(() -> {
          logger.warn("Usuário não encontrado: {}", username);
          return new UsernameNotFoundException("Usuário não encontrado");
        });
  }
}
