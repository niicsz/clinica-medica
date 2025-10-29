package com.example.clinica_medica.application.dto;

import org.springframework.http.ResponseCookie;

public class AuthResult {
  private final AuthResponse response;
  private final ResponseCookie cookie;

  public AuthResult(AuthResponse response, ResponseCookie cookie) {
    this.response = response;
    this.cookie = cookie;
  }

  public AuthResponse getResponse() {
    return response;
  }

  public ResponseCookie getCookie() {
    return cookie;
  }
}
