package com.example.clinica_medica.application.dto;

import com.example.clinica_medica.security.UserRole;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegistrationData {
  private final String nome;
  private final String cpf;
  private final Integer idade;
  private final String email;
  private final String senha;
  private final Set<UserRole> roles;

  public RegistrationData(
      String nome, String cpf, Integer idade, String email, String senha, Set<UserRole> roles) {
    this.nome = nome;
    this.cpf = cpf;
    this.idade = idade;
    this.email = email;
    this.senha = senha;
    this.roles =
        roles == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(roles));
  }

  public String getNome() {
    return nome;
  }

  public String getCpf() {
    return cpf;
  }

  public Integer getIdade() {
    return idade;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }
}
