package com.example.clinica_medica.controller.api;

import com.example.clinica_medica.entities.Consulta;
import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.entities.Paciente;
import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.services.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClinicaMedicaController {

    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final ConsultaService consultaService;

    public ClinicaMedicaController(
            UsuarioService usuarioService,
            PacienteService pacienteService,
            MedicoService medicoService,
            ConsultaService consultaService) {
        this.usuarioService = usuarioService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.consultaService = consultaService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.incluirUsuario(usuario));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obterTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> criarPaciente(@Valid @RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.incluirPaciente(paciente));
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> obterTodosPacientes() {
        return ResponseEntity.ok(pacienteService.listarTodosPacientes());
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> obterPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPacientePorId(id));
    }

    @GetMapping("/pacientes/cpf/{cpf}")
    public ResponseEntity<Paciente> obterPacientePorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.buscarPacientePorCpf(cpf));
    }

    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable Long id, @Valid @RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.atualizarPaciente(id, paciente));
    }

    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> removerPaciente(@PathVariable Long id) {
        pacienteService.excluirPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/medicos")
    public ResponseEntity<Medico> criarMedico(@Valid @RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.incluirMedico(medico));
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> obterTodosMedicos() {
        return ResponseEntity.ok(medicoService.listarTodosMedicos());
    }

    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> obterMedico(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarMedicoPorId(id));
    }

    @PutMapping("/medicos/{id}")
    public ResponseEntity<Medico> atualizarMedico(@PathVariable Long id, @Valid @RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.atualizarMedico(id, medico));
    }

    @DeleteMapping("/medicos/{id}")
    public ResponseEntity<Void> removerMedico(@PathVariable Long id) {
        medicoService.excluirMedico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/consultas")
    public ResponseEntity<Consulta> criarConsulta(@Valid @RequestBody Consulta consulta) {
        return ResponseEntity.ok(consultaService.agendarConsulta(consulta));
    }

    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> obterTodasConsultas() {
        return ResponseEntity.ok(consultaService.listarTodasConsultas());
    }

    @GetMapping("/consultas/{id}")
    public ResponseEntity<Consulta> obterConsulta(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarConsultaPorId(id));
    }

    @PutMapping("/consultas/{id}")
    public ResponseEntity<Consulta> atualizarConsulta(@PathVariable Long id, @Valid @RequestBody Consulta consulta) {
        return ResponseEntity.ok(consultaService.atualizarConsulta(id, consulta));
    }

    @DeleteMapping("/consultas/{id}")
    public ResponseEntity<Void> removerConsulta(@PathVariable Long id) {
        consultaService.excluirConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
