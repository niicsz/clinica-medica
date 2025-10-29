package com.example.clinica_medica.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medicos")
public class Medico {
  @Id private String id;

  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "Especialidade é obrigatória")
  private String especialidade;
}
