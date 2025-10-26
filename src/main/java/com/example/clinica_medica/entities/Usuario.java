package com.example.clinica_medica.entities;

import com.example.clinica_medica.security.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class Usuario {

  @Id private String id;

  @NotBlank(message = "Nome é Obrigatório")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @Indexed(unique = true)
  @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
  private String cpf;

  private Integer idade;

  @Email(message = "Email é inválido")
  @NotBlank(message = "Email é obrigatório")
  @Indexed(unique = true)
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String senha;

  private Set<UserRole> roles = new HashSet<>();

  public void setNome(String nome) {
    this.nome = nome;
  }
}
