package com.example.clinica_medica.generated.model;

import jakarta.annotation.Generated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Generated(value = "swagger-codegen", date = "2024-10-24")
public record LoginRequest(
    @Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String email,
    @NotBlank(message = "Senha é obrigatória") String senha) {}
