package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.security.JwtService;
import com.example.clinica_medica.security.JwtToken;
import com.example.clinica_medica.security.UsuarioDetails;
import com.example.clinica_medica.security.UserRole;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private static final String TOKEN_COOKIE_NAME = "jwt-token";

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtService jwtService;

  public AuthResult authenticate(String email, String senha) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha));

    if (!(authentication.getPrincipal() instanceof UsuarioDetails principal)) {
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
            new UsuarioResumo(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRoles()));

    return new AuthResult(response, cookie);
  }

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

  public record AuthResult(AuthResponse response, ResponseCookie cookie) {}

  public record AuthResponse(
      String token, Instant expiresAt, String tokenType, UsuarioResumo user) {}

  public record UsuarioResumo(
      String id, String nome, String email, Set<UserRole> roles) {}
}
