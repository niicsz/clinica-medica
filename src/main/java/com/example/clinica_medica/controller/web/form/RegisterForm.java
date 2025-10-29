package com.example.clinica_medica.controller.web.form;

import com.example.clinica_medica.security.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class RegisterForm {

  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
  private String cpf;

  @NotNull(message = "Idade é obrigatória")
  @Min(value = 0, message = "Idade deve ser positiva")
  private Integer idade;

  @Email(message = "Email inválido")
  @NotBlank(message = "Email é obrigatório")
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
  private String senha;

  @NotEmpty(message = "Selecione ao menos um perfil")
  private Set<UserRole> roles = new HashSet<>();

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Integer getIdade() {
    return idade;
  }

  public void setIdade(Integer idade) {
    this.idade = idade;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<UserRole> roles) {
    this.roles = roles;
  }
}
