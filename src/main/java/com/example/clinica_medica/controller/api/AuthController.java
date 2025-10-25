package com.example.clinica_medica.controller.api;

import com.example.clinica_medica.generated.api.AuthApi;
import com.example.clinica_medica.generated.model.LoginRequest;
import com.example.clinica_medica.services.AuthService;
import com.example.clinica_medica.services.AuthService.AuthResult;
import com.example.clinica_medica.services.AuthService.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  @Autowired private AuthService authService;

  @PostMapping("/login")
  @Override
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      AuthResult result = authService.authenticate(request.email(), request.senha());
      return ResponseEntity.ok()
          .header(HttpHeaders.SET_COOKIE, result.cookie().toString())
          .body(result.response());
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(401).build();
    }
  }

}
