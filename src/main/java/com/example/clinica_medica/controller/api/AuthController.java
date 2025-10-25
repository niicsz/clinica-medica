package com.example.clinica_medica.controller.api;

import com.example.clinica_medica.generated.api.AuthApi;
import com.example.clinica_medica.generated.model.LoginRequest;
import com.example.clinica_medica.generated.model.RegisterRequest;
import com.example.clinica_medica.security.UserRole;
import com.example.clinica_medica.services.AuthService;
import com.example.clinica_medica.services.AuthService.AuthResult;
import com.example.clinica_medica.services.AuthService.AuthResponse;
import com.example.clinica_medica.services.AuthService.RegistrationData;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

  @Autowired private AuthService authService;

  @Override
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      AuthResult result = authService.authenticate(request.email(), request.senha());
      return ResponseEntity.ok()
          .header(HttpHeaders.SET_COOKIE, result.cookie().toString())
          .body(result.response());
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @Override
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    try {
      Set<UserRole> roles = resolveRoles(request.roles());
      RegistrationData registrationData =
          new RegistrationData(
              request.nome(),
              request.cpf(),
              request.idade(),
              request.email(),
              request.senha(),
              roles);

      AuthResult result = authService.register(registrationData);
      return ResponseEntity.status(HttpStatus.CREATED)
          .header(HttpHeaders.SET_COOKIE, result.cookie().toString())
          .body(result.response());
    } catch (DuplicateKeyException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (DataIntegrityViolationException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  private Set<UserRole> resolveRoles(Set<String> roles) {
    if (roles == null || roles.isEmpty()) {
      return Collections.singleton(UserRole.RECEPCIONISTA);
    }

    try {
      return roles.stream().map(UserRole::valueOf).collect(Collectors.toSet());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Perfil de acesso inválido", ex);
    }
  }
}
