package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.application.port.in.PacienteUseCase;
import com.example.clinica_medica.domain.model.Paciente;
import com.example.clinica_medica.generated.web.WebPacienteApi;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebPacienteController implements WebPacienteApi {

  private final PacienteUseCase pacienteUseCase;

  public WebPacienteController(PacienteUseCase pacienteUseCase) {
    this.pacienteUseCase = pacienteUseCase;
  }

  @Override
  public String listarPacientes(Model model) {
    model.addAttribute("pacientes", pacienteUseCase.listarTodosPacientes());
    return "pacientes/lista";
  }

  @Override
  public String formNovoPaciente(Model model) {
    model.addAttribute("paciente", new Paciente());
    return "pacientes/form";
  }

  @Override
  public String salvarPaciente(
      @Valid @ModelAttribute("paciente") Paciente paciente,
      BindingResult result,
      RedirectAttributes attributes) {
    if (result.hasErrors()) {
      return "pacientes/form";
    }

    try {
      if (paciente.getId() != null && paciente.getId().trim().isEmpty()) {
        paciente.setId(null);
      }

      if (paciente.getId() == null) {
        pacienteUseCase.incluirPaciente(paciente);
        attributes.addFlashAttribute("mensagem", "Paciente cadastrado com sucesso!");
      } else {
        pacienteUseCase.atualizarPaciente(paciente.getId(), paciente);
        attributes.addFlashAttribute("mensagem", "Paciente atualizado com sucesso!");
      }
      return "redirect:/pacientes";
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar paciente: " + e.getMessage());
      return paciente.getId() == null
          ? "redirect:/pacientes/novo"
          : "redirect:/pacientes/editar/" + paciente.getId();
    }
  }

  @Override
  public String formEditarPaciente(@PathVariable String id, Model model) {
    Paciente paciente = pacienteUseCase.buscarPacientePorId(id);
    if (paciente == null) {
      return "redirect:/pacientes";
    }
    model.addAttribute("paciente", paciente);
    return "pacientes/form";
  }

  @Override
  public String excluirPaciente(@PathVariable String id, RedirectAttributes attributes) {
    try {
      pacienteUseCase.excluirPaciente(id);
      attributes.addFlashAttribute("mensagem", "Paciente excluído com sucesso!");
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao excluir paciente: " + e.getMessage());
    }
    return "redirect:/pacientes";
  }
}
