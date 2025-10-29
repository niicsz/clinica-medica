package com.example.clinica_medica.application.dto;

import java.time.Instant;

public class AuthResponse {
  private final String token;
  private final Instant expiresAt;
  private final String tokenType;
  private final UsuarioResumo user;

  public AuthResponse(String token, Instant expiresAt, String tokenType, UsuarioResumo user) {
    this.token = token;
    this.expiresAt = expiresAt;
    this.tokenType = tokenType;
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public String getTokenType() {
    return tokenType;
  }

  public UsuarioResumo getUser() {
    return user;
  }
}
