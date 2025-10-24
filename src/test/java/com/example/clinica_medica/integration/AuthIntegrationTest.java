package com.example.clinica_medica.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.repositories.UsuarioRepository;
import com.example.clinica_medica.security.UserRole;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

  private static final String EMAIL = "admin@test.com";
  private static final String PASSWORD = "SenhaForte123";

  @Autowired private MockMvc mockMvc;

  @Autowired private UsuarioRepository usuarioRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    usuarioRepository.deleteAll();
    Usuario usuario = new Usuario();
    usuario.setNome("Administrador Teste");
    usuario.setCpf("12345678901");
    usuario.setIdade(30);
    usuario.setEmail(EMAIL);
    usuario.setSenha(passwordEncoder.encode(PASSWORD));
    usuario.setRoles(Set.of(UserRole.ADMIN));
    usuarioRepository.save(usuario);
  }

  @Test
  void shouldAuthenticateAndReturnJwtAndCookie() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper
                        .createObjectNode()
                        .put("email", EMAIL)
                        .put("senha", PASSWORD)
                        .toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.user.email").value(EMAIL))
        .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("jwt-token=")));
  }

  @Test
  void shouldPreventAccessWithoutToken() throws Exception {
    mockMvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldAllowAccessWithValidToken() throws Exception {
    MvcResult loginResult =
        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper
                            .createObjectNode()
                            .put("email", EMAIL)
                            .put("senha", PASSWORD)
                            .toString()))
            .andExpect(status().isOk())
            .andReturn();

    JsonNode body = objectMapper.readTree(loginResult.getResponse().getContentAsString());
    String token = body.get("token").asText();

    MvcResult result =
        mockMvc
            .perform(get("/api/usuarios").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn();

    JsonNode usuarios = objectMapper.readTree(result.getResponse().getContentAsString());
    assertThat(usuarios.isArray()).isTrue();
    assertThat(usuarios).isNotEmpty();
  }
}
