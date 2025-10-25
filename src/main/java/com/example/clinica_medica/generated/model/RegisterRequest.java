package com.example.clinica_medica.generated.model;

import jakarta.annotation.Generated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Generated(value = "swagger-codegen", date = "2024-10-24")
public record RegisterRequest(
    @NotBlank(message = "Nome é obrigatório") String nome,
    @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
        String cpf,
    @NotNull(message = "Idade é obrigatória") @Min(value = 0, message = "Idade deve ser positiva") Integer idade,
    @Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String email,
    @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senha,
    Set<String> roles) {}
