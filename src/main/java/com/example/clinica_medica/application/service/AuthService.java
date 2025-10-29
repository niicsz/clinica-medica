package com.example.clinica_medica.application.service;

import com.example.clinica_medica.application.dto.AuthResponse;
import com.example.clinica_medica.application.dto.AuthResult;
import com.example.clinica_medica.application.dto.RegistrationData;
import com.example.clinica_medica.application.dto.UsuarioResumo;
import com.example.clinica_medica.application.port.in.AuthUseCase;
import com.example.clinica_medica.application.port.in.UsuarioUseCase;
import com.example.clinica_medica.domain.model.Usuario;
import com.example.clinica_medica.security.JwtService;
import com.example.clinica_medica.security.JwtToken;
import com.example.clinica_medica.security.UserRole;
import com.example.clinica_medica.security.UsuarioDetails;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private static final String TOKEN_COOKIE_NAME = "jwt-token";

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UsuarioUseCase usuarioUseCase;

  public AuthService(
      AuthenticationManager authenticationManager,
      JwtService jwtService,
      UsuarioUseCase usuarioUseCase) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.usuarioUseCase = usuarioUseCase;
  }

  @Override
  public AuthResult authenticate(String email, String senha) {
    logger.info("Tentativa de autenticação para o email: {}", email);

    Authentication authentication =
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));

    if (!(authentication.getPrincipal() instanceof UsuarioDetails principal)) {
      logger.error(
          "Falha na autenticação: Principal não é uma instância de UsuarioDetails para email: {}",
          email);
      throw new UsernameNotFoundException("Usuário não encontrado");
    }

    JwtToken jwtToken = jwtService.generateToken(principal);
    Usuario usuario = principal.getUsuario();
    ResponseCookie cookie = buildAuthCookie(jwtToken);

    AuthResponse response =
        new AuthResponse(
            jwtToken.value(),
            jwtToken.expiresAt(),
            "Bearer",
            new UsuarioResumo(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRoles()));

    logger.info("Autenticação bem-sucedida para o usuário: {} (ID: {})", email, usuario.getId());
    return new AuthResult(response, cookie);
  }

  @Override
  public AuthResult register(RegistrationData registrationData) {
    logger.info(
        "Tentativa de registro para o email: {} com nome: {}",
        registrationData.getEmail(),
        registrationData.getNome());

    Usuario usuario = new Usuario();
    usuario.setNome(registrationData.getNome());
    usuario.setCpf(registrationData.getCpf());
    usuario.setIdade(registrationData.getIdade());
    usuario.setEmail(registrationData.getEmail());
    usuario.setSenha(registrationData.getSenha());
    usuario.setRoles(normalizeRoles(registrationData.getRoles()));

    usuarioUseCase.incluirUsuario(usuario);
    logger.info(
        "Usuário registrado com sucesso: {} (CPF: {})",
        registrationData.getEmail(),
        registrationData.getCpf());

    return authenticate(registrationData.getEmail(), registrationData.getSenha());
  }

  @Override
  public ResponseCookie logoutCookie() {
    return ResponseCookie.from(TOKEN_COOKIE_NAME, "")
        .httpOnly(true)
        .secure(false)
        .sameSite("Lax")
        .path("/")
        .maxAge(Duration.ZERO)
        .build();
  }

  private ResponseCookie buildAuthCookie(JwtToken token) {
    long maxAge = Math.max(0, Duration.between(Instant.now(), token.expiresAt()).getSeconds());
    return ResponseCookie.from(TOKEN_COOKIE_NAME, token.value())
        .httpOnly(true)
        .secure(false)
        .sameSite("Lax")
        .path("/")
        .maxAge(maxAge)
        .build();
  }

  private Set<UserRole> normalizeRoles(Set<UserRole> roles) {
    if (roles == null || roles.isEmpty()) {
      return Set.of(UserRole.RECEPCIONISTA);
    }

    return new HashSet<>(roles);
  }
}
