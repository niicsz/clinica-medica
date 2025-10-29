package com.example.clinica_medica.generated.api;

import com.example.clinica_medica.application.dto.AuthResponse;
import com.example.clinica_medica.generated.model.LoginRequest;
import com.example.clinica_medica.generated.model.RegisterRequest;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Generated(value = "swagger-codegen", date = "2024-10-24")
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public interface AuthApi {

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request);

  @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request);
}
