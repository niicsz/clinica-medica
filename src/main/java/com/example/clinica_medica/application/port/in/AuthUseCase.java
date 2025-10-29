package com.example.clinica_medica.application.port.in;

import com.example.clinica_medica.application.dto.AuthResult;
import com.example.clinica_medica.application.dto.RegistrationData;
import org.springframework.http.ResponseCookie;

public interface AuthUseCase {
  AuthResult authenticate(String email, String senha);

  AuthResult register(RegistrationData registrationData);

  ResponseCookie logoutCookie();
}
