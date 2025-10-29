package com.example.clinica_medica.domain.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultas")
public class Consulta {
  @Id private String id;

  @NotNull(message = "Paciente é obrigatório")
  @DBRef
  private Paciente paciente;

  @NotNull(message = "Médico é obrigatório")
  @DBRef
  private Medico medico;

  @NotNull(message = "Data e hora são obrigatórias")
  private LocalDateTime dataHora;

  @NotNull(message = "Tipo de consulta é obrigatório")
  private String tipoConsulta;
}
