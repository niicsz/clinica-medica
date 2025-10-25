package com.example.clinica_medica.generated.api;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Generated(value = "swagger-codegen", date = "2024-10-24")
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ClinicaMedicaApi {

  @PostMapping(value = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario);

  @GetMapping("/usuarios")
  ResponseEntity<List<Usuario>> listarUsuarios();

  @PutMapping(value = "/usuarios/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Usuario> atualizarUsuario(
      @PathVariable("id") String id, @Valid @RequestBody Usuario usuario);

  @DeleteMapping("/usuarios/{id}")
  ResponseEntity<Void> excluirUsuario(@PathVariable("id") String id);

  @GetMapping("/usuarios/{id}")
  ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable("id") String id);

  @PostMapping(value = "/pacientes", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Paciente> cadastrarPaciente(@Valid @RequestBody Paciente paciente);

  @GetMapping("/pacientes")
  ResponseEntity<List<Paciente>> listarPacientes();

  @GetMapping("/pacientes/cpf/{cpf}")
  ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable("cpf") String cpf);

  @PutMapping(value = "/pacientes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Paciente> atualizarPaciente(
      @PathVariable("id") String id, @Valid @RequestBody Paciente paciente);

  @DeleteMapping("/pacientes/{id}")
  ResponseEntity<Void> excluirPaciente(@PathVariable("id") String id);

  @GetMapping("/pacientes/{id}")
  ResponseEntity<Paciente> buscarPacientePorId(@PathVariable("id") String id);

  @PostMapping(value = "/medicos", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Medico> cadastrarMedico(@Valid @RequestBody Medico medico);

  @GetMapping("/medicos")
  ResponseEntity<List<Medico>> listarMedicos();

  @GetMapping("/medicos/{id}")
  ResponseEntity<Medico> buscarMedicoPorId(@PathVariable("id") String id);

  @PutMapping(value = "/medicos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Medico> atualizarMedico(
      @PathVariable("id") String id, @Valid @RequestBody Medico medico);

  @DeleteMapping("/medicos/{id}")
  ResponseEntity<Void> excluirMedico(@PathVariable("id") String id);

  @PostMapping(value = "/consultas", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Consulta> agendarConsulta(@Valid @RequestBody Consulta consulta);

  @GetMapping("/consultas")
  ResponseEntity<List<Consulta>> listarConsultas();

  @PutMapping(value = "/consultas/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Consulta> atualizarConsulta(
      @PathVariable("id") String id, @Valid @RequestBody Consulta consulta);

  @DeleteMapping("/consultas/{id}")
  ResponseEntity<Void> excluirConsulta(@PathVariable("id") String id);

  @GetMapping("/consultas/{id}")
  ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable("id") String id);
}
