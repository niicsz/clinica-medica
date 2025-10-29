package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.application.port.in.MedicoUseCase;
import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.generated.web.WebMedicoApi;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebMedicoController implements WebMedicoApi {

  private final MedicoUseCase medicoUseCase;

  public WebMedicoController(MedicoUseCase medicoUseCase) {
    this.medicoUseCase = medicoUseCase;
  }

  @Override
  public String listarMedicos(Model model) {
    model.addAttribute("medicos", medicoUseCase.listarTodosMedicos());
    return "medicos/lista";
  }

  @Override
  public String formNovoMedico(Model model) {
    model.addAttribute("medico", new Medico());
    return "medicos/form";
  }

  @Override
  public String salvarMedico(
      @Valid @ModelAttribute("medico") Medico medico,
      BindingResult result,
      RedirectAttributes attributes) {
    if (result.hasErrors()) {
      return "medicos/form";
    }

    try {
      if (medico.getId() == null) {
        medicoUseCase.incluirMedico(medico);
        attributes.addFlashAttribute("mensagem", "Médico cadastrado com sucesso!");
      } else {
        medicoUseCase.atualizarMedico(medico.getId(), medico);
        attributes.addFlashAttribute("mensagem", "Médico atualizado com sucesso!");
      }
      return "redirect:/medicos";
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar médico: " + e.getMessage());
      return medico.getId() == null
          ? "redirect:/medicos/novo"
          : "redirect:/medicos/editar/" + medico.getId();
    }
  }

  @Override
  public String formEditarMedico(@PathVariable String id, Model model) {
    Medico medico = medicoUseCase.buscarMedicoPorId(id);
    if (medico == null) {
      return "redirect:/medicos";
    }
    model.addAttribute("medico", medico);
    return "medicos/form";
  }

  @Override
  public String excluirMedico(@PathVariable String id, RedirectAttributes attributes) {
    try {
      medicoUseCase.excluirMedico(id);
      attributes.addFlashAttribute("mensagem", "Médico excluído com sucesso!");
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao excluir médico: " + e.getMessage());
    }
    return "redirect:/medicos";
  }
}
