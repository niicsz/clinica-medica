package com.example.clinica_medica.config;

import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.services.MedicoService;
import com.example.clinica_medica.services.PacienteService;
import com.example.clinica_medica.services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final UsuarioService usuarioService;

    public TestConfig(
            MedicoService medicoService,
            PacienteService pacienteService,
            UsuarioService usuarioService) {
        this.medicoService = medicoService;
        this.pacienteService = pacienteService;
        this.usuarioService = usuarioService;
    }

    @Bean
    public CommandLineRunner initTestData() {
        return args -> {
            criarMedicos();
            criarPacientes();
            criarUsuarios();
        };
    }

    private void criarMedicos() {
        var medicos = new Medico[] {
            criarMedico("Dr. João Silva", "Cardiologia"),
            criarMedico("Dra. Maria Souza", "Pediatria"),
            criarMedico("Dr. Pedro Santos", "Ortopedia")
        };

        for (Medico medico : medicos) {
            medicoService.incluirMedico(medico);
        }
    }

    private void criarPacientes() {
        var pacientes = new Paciente[] {
            criarPaciente("Ana Oliveira", "12345678901", 35, "ana@email.com"),
            criarPaciente("Carlos Pereira", "98765432109", 42, "carlos@email.com"),
            criarPaciente("Mariana Costa", "45678912345", 28, "mariana@email.com")
        };

        for (Paciente paciente : pacientes) {
            pacienteService.incluirPaciente(paciente);
        }
    }

    private void criarUsuarios() {
        var usuarios = new Usuario[] {
            criarUsuario("Admin", "11111111111", 40, "admin@clinica.com", "senha123"),
            criarUsuario("Recepcionista", "22222222222", 35, "recepcao@clinica.com", "senha456")
        };

        for (Usuario usuario : usuarios) {
            usuarioService.incluirUsuario(usuario);
        }
    }

    private Medico criarMedico(String nome, String especialidade) {
        var medico = new Medico();
        medico.setNome(nome);
        medico.setEspecialidade(especialidade);
        return medico;
    }

    private Paciente criarPaciente(String nome, String cpf, int idade, String email) {
        var paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setCpf(cpf);
        paciente.setIdade(idade);
        paciente.setEmail(email);
        return paciente;
    }

    private Usuario criarUsuario(String nome, String cpf, int idade, String email, String senha) {
        var usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setIdade(idade);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }
}
