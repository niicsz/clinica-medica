package com.example.clinica_medica.config;

import com.example.clinica_medica.application.port.in.MedicoUseCase;
import com.example.clinica_medica.application.port.in.PacienteUseCase;
import com.example.clinica_medica.application.port.in.UsuarioUseCase;
import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.domain.model.Paciente;
import com.example.clinica_medica.domain.model.Usuario;
import com.example.clinica_medica.security.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

  private final MedicoUseCase medicoUseCase;

  private final PacienteUseCase pacienteUseCase;

  private final UsuarioUseCase usuarioUseCase;

  public TestConfig(
      MedicoUseCase medicoUseCase, PacienteUseCase pacienteUseCase, UsuarioUseCase usuarioUseCase) {
    this.medicoUseCase = medicoUseCase;
    this.pacienteUseCase = pacienteUseCase;
    this.usuarioUseCase = usuarioUseCase;
  }

  @Bean
  public CommandLineRunner initTestData() {
    return args -> {
      Medico medico1 = new Medico();
      medico1.setNome("Dr. João Silva");
      medico1.setEspecialidade("Cardiologia");
      medicoUseCase.incluirMedico(medico1);

      Medico medico2 = new Medico();
      medico2.setNome("Dra. Maria Souza");
      medico2.setEspecialidade("Pediatria");
      medicoUseCase.incluirMedico(medico2);

      Medico medico3 = new Medico();
      medico3.setNome("Dr. Pedro Santos");
      medico3.setEspecialidade("Ortopedia");
      medicoUseCase.incluirMedico(medico3);

      Paciente paciente1 = new Paciente();
      paciente1.setNome("Ana Oliveira");
      paciente1.setCpf("12345678901");
      paciente1.setIdade(35);
      paciente1.setEmail("ana@email.com");
      pacienteUseCase.incluirPaciente(paciente1);

      Paciente paciente2 = new Paciente();
      paciente2.setNome("Carlos Pereira");
      paciente2.setCpf("98765432109");
      paciente2.setIdade(42);
      paciente2.setEmail("carlos@email.com");
      pacienteUseCase.incluirPaciente(paciente2);

      Paciente paciente3 = new Paciente();
      paciente3.setNome("Mariana Costa");
      paciente3.setCpf("45678912345");
      paciente3.setIdade(28);
      paciente3.setEmail("mariana@email.com");
      pacienteUseCase.incluirPaciente(paciente3);

      Usuario usuario1 = new Usuario();
      usuario1.setNome("Admin");
      usuario1.setCpf("11111111111");
      usuario1.setIdade(40);
      usuario1.setEmail("admin@clinica.com");
      usuario1.setSenha("Senha123!");
      usuario1.setRoles(java.util.Set.of(UserRole.ADMIN));
      usuarioUseCase.incluirUsuario(usuario1);

      Usuario usuario2 = new Usuario();
      usuario2.setNome("Recepcionista");
      usuario2.setCpf("22222222222");
      usuario2.setIdade(35);
      usuario2.setEmail("recepcao@clinica.com");
      usuario2.setSenha("Senha456!");
      usuario2.setRoles(java.util.Set.of(UserRole.RECEPCIONISTA));
      usuarioUseCase.incluirUsuario(usuario2);
    };
  }
}
