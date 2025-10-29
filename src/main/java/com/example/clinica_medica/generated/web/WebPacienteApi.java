package com.example.clinica_medica.generated.web;

import com.example.clinica_medica.domain.model.Paciente;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Generated(value = "swagger-codegen", date = "2024-10-24")
@RequestMapping("/pacientes")
public interface WebPacienteApi {

  @GetMapping
  String listarPacientes(Model model);

  @GetMapping("/novo")
  String formNovoPaciente(Model model);

  @PostMapping("/salvar")
  String salvarPaciente(
      @Valid @ModelAttribute("paciente") Paciente paciente,
      BindingResult result,
      RedirectAttributes attributes);

  @GetMapping("/editar/{id}")
  String formEditarPaciente(@PathVariable("id") String id, Model model);

  @GetMapping("/excluir/{id}")
  String excluirPaciente(@PathVariable("id") String id, RedirectAttributes attributes);
}
