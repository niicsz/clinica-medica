package com.example.clinica_medica.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pacientes")
public class Paciente {
  @Id private String id;

  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @Indexed(unique = true)
  @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
  private String cpf;

  private Integer idade;

  @Email(message = "Email inválido")
  @NotBlank(message = "Email é obrigatório")
  private String email;
}
