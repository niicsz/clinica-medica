package com.example.clinica_medica.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClinicaMedicaControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void deveCriarEObterUsuario() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setNome("João Silva");
    usuario.setCpf("52998224725");
    usuario.setIdade(30);
    usuario.setEmail("joao@email.com");
    usuario.setSenha("senha123");

    String usuarioJson = objectMapper.writeValueAsString(usuario);

    String responseContent =
        mockMvc
            .perform(
                post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(usuarioJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.cpf").value("52998224725"))
            .andExpect(jsonPath("$.idade").value(30))
            .andExpect(jsonPath("$.email").value("joao@email.com"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Usuario usuarioCriado = objectMapper.readValue(responseContent, Usuario.class);

    mockMvc
        .perform(get("/api/usuarios/" + usuarioCriado.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(usuarioCriado.getId()))
        .andExpect(jsonPath("$.nome").value("João Silva"))
        .andExpect(jsonPath("$.cpf").value("52998224725"));
  }

  @Test
  void deveCriarEObterPaciente() throws Exception {
    Paciente paciente = new Paciente();
    paciente.setNome("Maria Santos");
    paciente.setCpf("87748248701");
    paciente.setIdade(28);
    paciente.setEmail("maria@email.com");

    String pacienteJson = objectMapper.writeValueAsString(paciente);

    String responseContent =
        mockMvc
            .perform(
                post("/api/pacientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(pacienteJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("Maria Santos"))
            .andExpect(jsonPath("$.cpf").value("87748248701"))
            .andExpect(jsonPath("$.idade").value(28))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Paciente pacienteCriado = objectMapper.readValue(responseContent, Paciente.class);

    mockMvc
        .perform(get("/api/pacientes/" + pacienteCriado.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pacienteCriado.getId()))
        .andExpect(jsonPath("$.nome").value("Maria Santos"))
        .andExpect(jsonPath("$.idade").value(28));
  }

  @Test
  void deveCriarEObterMedico() throws Exception {
    Medico medico = new Medico();
    medico.setNome("Dr. Pedro");
    medico.setEspecialidade("Cardiologia");

    String medicoJson = objectMapper.writeValueAsString(medico);

    String responseContent =
        mockMvc
            .perform(
                post("/api/medicos").contentType(MediaType.APPLICATION_JSON).content(medicoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("Dr. Pedro"))
            .andExpect(jsonPath("$.especialidade").value("Cardiologia"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Medico medicoCriado = objectMapper.readValue(responseContent, Medico.class);

    mockMvc
        .perform(get("/api/medicos/" + medicoCriado.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(medicoCriado.getId()))
        .andExpect(jsonPath("$.nome").value("Dr. Pedro"))
        .andExpect(jsonPath("$.especialidade").value("Cardiologia"));
  }

  @Test
  void deveCriarEObterConsulta() throws Exception {
    Paciente paciente = new Paciente();
    paciente.setNome("Ana Silva");
    paciente.setCpf("86865575780");
    paciente.setIdade(32);
    paciente.setEmail("ana@email.com");

    Medico medico = new Medico();
    medico.setNome("Dra. Clara");
    medico.setEspecialidade("Clínica Geral");

    String pacienteCriado =
        mockMvc
            .perform(
                post("/api/pacientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paciente)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String medicoCriado =
        mockMvc
            .perform(
                post("/api/medicos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Paciente pacienteResponse = objectMapper.readValue(pacienteCriado, Paciente.class);
    Medico medicoResponse = objectMapper.readValue(medicoCriado, Medico.class);

    Consulta consulta = new Consulta();
    consulta.setPaciente(pacienteResponse);
    consulta.setMedico(medicoResponse);
    consulta.setDataHora(LocalDateTime.now().plusDays(1));
    consulta.setTipoConsulta("Rotina");

    String consultaJson = objectMapper.writeValueAsString(consulta);

    String responseContent =
        mockMvc
            .perform(
                post("/api/consultas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(consultaJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.tipoConsulta").value("Rotina"))
            .andExpect(jsonPath("$.paciente.idade").value(32))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Consulta consultaCriada = objectMapper.readValue(responseContent, Consulta.class);

    mockMvc
        .perform(get("/api/consultas/" + consultaCriada.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(consultaCriada.getId()))
        .andExpect(jsonPath("$.tipoConsulta").value("Rotina"))
        .andExpect(jsonPath("$.paciente.idade").value(32));
  }

  @Test
  void deveAtualizarUsuario() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setNome("Carlos Silva");
    usuario.setCpf("53575055866");
    usuario.setIdade(25);
    usuario.setEmail("carlos@email.com");
    usuario.setSenha("senha123");

    String responseContent =
        mockMvc
            .perform(
                post("/api/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Usuario usuarioCriado = objectMapper.readValue(responseContent, Usuario.class);

    usuarioCriado.setNome("Carlos Silva Jr");
    usuarioCriado.setIdade(26);

    mockMvc
        .perform(
            put("/api/usuarios/" + usuarioCriado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioCriado)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Carlos Silva Jr"))
        .andExpect(jsonPath("$.idade").value(26));
  }

  @Test
  void deveRemoverUsuario() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setNome("José Silva");
    usuario.setCpf("73401677730");
    usuario.setIdade(40);
    usuario.setEmail("jose@email.com");
    usuario.setSenha("senha123");

    String responseContent =
        mockMvc
            .perform(
                post("/api/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Usuario usuarioCriado = objectMapper.readValue(responseContent, Usuario.class);

    mockMvc
        .perform(delete("/api/usuarios/" + usuarioCriado.getId()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/usuarios/" + usuarioCriado.getId())).andExpect(status().isNotFound());
  }
}
