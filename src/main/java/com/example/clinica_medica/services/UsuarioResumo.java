package com.example.clinica_medica.services;

import com.example.clinica_medica.security.UserRole;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UsuarioResumo {
  private final String id;
  private final String nome;
  private final String email;
  private final Set<UserRole> roles;

  public UsuarioResumo(String id, String nome, String email, Set<UserRole> roles) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.roles =
        roles == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(roles));
  }

  public String getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }
}
