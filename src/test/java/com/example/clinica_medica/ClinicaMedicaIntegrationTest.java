package com.example.clinica_medica;

import static org.junit.jupiter.api.Assertions.*;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ClinicaMedicaIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  private ObjectMapper objectMapper;

  // Test data IDs - will be populated during tests
  private static Integer usuarioId;
  private static Long pacienteId;
  private static Long medicoId;
  private static Long consultaId;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  // === USUARIO TESTS ===

  @Test
  @Order(1)
  void testCriarUsuario() {
    Usuario usuario = new Usuario();
    usuario.setNome("João Silva");
    usuario.setCpf("11144477735"); // Valid CPF for testing
    usuario.setIdade(30);
    usuario.setEmail("joao@email.com");
    usuario.setSenha("123456");

    ResponseEntity<Usuario> response =
        restTemplate.postForEntity("/api/usuarios", usuario, Usuario.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    usuarioId = response.getBody().getId();
    assertEquals("João Silva", response.getBody().getNome());
    assertEquals("11144477735", response.getBody().getCpf());
    assertEquals("joao@email.com", response.getBody().getEmail());
  }

  @Test
  @Order(2)
  void testListarUsuarios() {
    ResponseEntity<List> response = restTemplate.getForEntity("/api/usuarios", List.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() > 0);
  }

  @Test
  @Order(3)
  void testBuscarUsuarioPorId() {
    ResponseEntity<Usuario> response =
        restTemplate.getForEntity("/api/usuarios/" + usuarioId, Usuario.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("João Silva", response.getBody().getNome());
  }

  @Test
  @Order(4)
  void testAtualizarUsuario() {
    Usuario usuario = new Usuario();
    usuario.setNome("João Santos");
    usuario.setCpf("11144477735"); // Same CPF, just updating other fields
    usuario.setIdade(31);
    usuario.setEmail("joao.santos@email.com");
    usuario.setSenha("123456");

    HttpEntity<Usuario> request = new HttpEntity<>(usuario);
    ResponseEntity<Usuario> response =
        restTemplate.exchange("/api/usuarios/" + usuarioId, HttpMethod.PUT, request, Usuario.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("João Santos", response.getBody().getNome());
    assertEquals("joao.santos@email.com", response.getBody().getEmail());
    assertEquals(31, response.getBody().getIdade());
  }

  // === PACIENTE TESTS ===

  @Test
  @Order(5)
  void testCriarPaciente() {
    Paciente paciente = new Paciente();
    paciente.setNome("Maria Silva");
    paciente.setCpf("11144477736"); // Different valid CPF
    paciente.setIdade(25);
    paciente.setEmail("maria@email.com");

    ResponseEntity<Paciente> response =
        restTemplate.postForEntity("/api/pacientes", paciente, Paciente.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    pacienteId = response.getBody().getId();
    assertEquals("Maria Silva", response.getBody().getNome());
    assertEquals("11144477736", response.getBody().getCpf());
    assertEquals("maria@email.com", response.getBody().getEmail());
  }

  @Test
  @Order(6)
  void testListarPacientes() {
    ResponseEntity<List> response = restTemplate.getForEntity("/api/pacientes", List.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() > 0);
  }

  @Test
  @Order(7)
  void testBuscarPacientePorId() {
    ResponseEntity<Paciente> response =
        restTemplate.getForEntity("/api/pacientes/" + pacienteId, Paciente.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Maria Silva", response.getBody().getNome());
  }

  @Test
  @Order(8)
  void testBuscarPacientePorCpf() {
    ResponseEntity<Paciente> response =
        restTemplate.getForEntity("/api/pacientes/cpf/11144477736", Paciente.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Maria Silva", response.getBody().getNome());
  }

  @Test
  @Order(9)
  void testAtualizarPaciente() {
    Paciente paciente = new Paciente();
    paciente.setNome("Maria Santos");
    paciente.setCpf("11144477736"); // Same CPF, just updating other fields
    paciente.setIdade(26);
    paciente.setEmail("maria.santos@email.com");

    HttpEntity<Paciente> request = new HttpEntity<>(paciente);
    ResponseEntity<Paciente> response =
        restTemplate.exchange(
            "/api/pacientes/" + pacienteId, HttpMethod.PUT, request, Paciente.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Maria Santos", response.getBody().getNome());
    assertEquals("maria.santos@email.com", response.getBody().getEmail());
  }

  // === MEDICO TESTS ===

  @Test
  @Order(10)
  void testCriarMedico() {
    Medico medico = new Medico();
    medico.setNome("Dr. Carlos Pereira");
    medico.setEspecialidade("Cardiologia");

    ResponseEntity<Medico> response =
        restTemplate.postForEntity("/api/medicos", medico, Medico.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    medicoId = response.getBody().getId();
    assertEquals("Dr. Carlos Pereira", response.getBody().getNome());
    assertEquals("Cardiologia", response.getBody().getEspecialidade());
  }

  @Test
  @Order(11)
  void testListarMedicos() {
    ResponseEntity<List> response = restTemplate.getForEntity("/api/medicos", List.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() > 0);
  }

  @Test
  @Order(12)
  void testBuscarMedicoPorId() {
    ResponseEntity<Medico> response =
        restTemplate.getForEntity("/api/medicos/" + medicoId, Medico.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Dr. Carlos Pereira", response.getBody().getNome());
  }

  @Test
  @Order(13)
  void testAtualizarMedico() {
    Medico medico = new Medico();
    medico.setNome("Dr. Carlos Silva");
    medico.setEspecialidade("Cardiologia Clínica");

    HttpEntity<Medico> request = new HttpEntity<>(medico);
    ResponseEntity<Medico> response =
        restTemplate.exchange("/api/medicos/" + medicoId, HttpMethod.PUT, request, Medico.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Dr. Carlos Silva", response.getBody().getNome());
    assertEquals("Cardiologia Clínica", response.getBody().getEspecialidade());
  }

  // === CONSULTA TESTS ===

  @Test
  @Order(14)
  void testAgendarConsulta() {
    Consulta consulta = new Consulta();

    // Set up relationships
    Paciente paciente = new Paciente();
    paciente.setId(pacienteId);
    consulta.setPaciente(paciente);

    Medico medico = new Medico();
    medico.setId(medicoId);
    consulta.setMedico(medico);

    consulta.setDataHora(LocalDateTime.now().plusDays(1));
    consulta.setTipoConsulta("Consulta Regular");

    ResponseEntity<Consulta> response =
        restTemplate.postForEntity("/api/consultas", consulta, Consulta.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    consultaId = response.getBody().getId();
    assertEquals("Consulta Regular", response.getBody().getTipoConsulta());
    assertNotNull(response.getBody().getPaciente());
    assertNotNull(response.getBody().getMedico());
  }

  @Test
  @Order(15)
  void testListarConsultas() {
    ResponseEntity<List> response = restTemplate.getForEntity("/api/consultas", List.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() > 0);
  }

  @Test
  @Order(16)
  void testBuscarConsultaPorId() {
    ResponseEntity<Consulta> response =
        restTemplate.getForEntity("/api/consultas/" + consultaId, Consulta.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Consulta Regular", response.getBody().getTipoConsulta());
  }

  @Test
  @Order(17)
  void testAtualizarConsulta() {
    Consulta consulta = new Consulta();

    // Set up relationships
    Paciente paciente = new Paciente();
    paciente.setId(pacienteId);
    consulta.setPaciente(paciente);

    Medico medico = new Medico();
    medico.setId(medicoId);
    consulta.setMedico(medico);

    consulta.setDataHora(LocalDateTime.now().plusDays(2));
    consulta.setTipoConsulta("Consulta de Retorno");

    HttpEntity<Consulta> request = new HttpEntity<>(consulta);
    ResponseEntity<Consulta> response =
        restTemplate.exchange(
            "/api/consultas/" + consultaId, HttpMethod.PUT, request, Consulta.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Consulta de Retorno", response.getBody().getTipoConsulta());
  }

  // === VALIDATION TESTS ===

  @Test
  @Order(18)
  void testCriarUsuarioComCpfInvalido() {
    Usuario usuario = new Usuario();
    usuario.setNome("Teste Usuario");
    usuario.setCpf("123"); // CPF inválido - muito curto
    usuario.setIdade(30);
    usuario.setEmail("teste@email.com");
    usuario.setSenha("123456");

    ResponseEntity<String> response =
        restTemplate.postForEntity("/api/usuarios", usuario, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // The ValidationService throws an exception that gets converted to a bad request
    assertNotNull(response.getBody());
  }

  @Test
  @Order(19)
  void testCriarUsuarioComEmailInvalido() {
    Usuario usuario = new Usuario();
    usuario.setNome("Teste Usuario");
    usuario.setCpf("11144477736"); // Different valid CPF
    usuario.setIdade(30);
    usuario.setEmail("email-invalido"); // Email inválido
    usuario.setSenha("123456");

    ResponseEntity<String> response =
        restTemplate.postForEntity("/api/usuarios", usuario, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // The ValidationService throws an exception that gets converted to a bad request
    assertNotNull(response.getBody());
  }

  // === CLEANUP TESTS ===

  @Test
  @Order(20)
  void testExcluirConsulta() {
    ResponseEntity<Void> response =
        restTemplate.exchange("/api/consultas/" + consultaId, HttpMethod.DELETE, null, Void.class);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Verify deletion
    ResponseEntity<Consulta> getResponse =
        restTemplate.getForEntity("/api/consultas/" + consultaId, Consulta.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }

  @Test
  @Order(21)
  void testExcluirMedico() {
    ResponseEntity<Void> response =
        restTemplate.exchange("/api/medicos/" + medicoId, HttpMethod.DELETE, null, Void.class);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Verify deletion
    ResponseEntity<Medico> getResponse =
        restTemplate.getForEntity("/api/medicos/" + medicoId, Medico.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }

  @Test
  @Order(22)
  void testExcluirPaciente() {
    ResponseEntity<Void> response =
        restTemplate.exchange("/api/pacientes/" + pacienteId, HttpMethod.DELETE, null, Void.class);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Verify deletion
    ResponseEntity<Paciente> getResponse =
        restTemplate.getForEntity("/api/pacientes/" + pacienteId, Paciente.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }

  @Test
  @Order(23)
  void testExcluirUsuario() {
    ResponseEntity<Void> response =
        restTemplate.exchange("/api/usuarios/" + usuarioId, HttpMethod.DELETE, null, Void.class);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Verify deletion
    ResponseEntity<Usuario> getResponse =
        restTemplate.getForEntity("/api/usuarios/" + usuarioId, Usuario.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }
}
