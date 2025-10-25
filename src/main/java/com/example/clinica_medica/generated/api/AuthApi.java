package com.example.clinica_medica.generated.api;

import com.example.clinica_medica.services.AuthService.AuthResponse;
import com.example.clinica_medica.generated.model.LoginRequest;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Generated(value = "swagger-codegen", date = "2024-10-24")
public interface AuthApi {

  ResponseEntity<AuthResponse> login(@Valid LoginRequest request);
}
