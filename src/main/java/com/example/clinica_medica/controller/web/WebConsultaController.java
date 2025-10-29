package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.application.port.in.ConsultaUseCase;
import com.example.clinica_medica.application.port.in.MedicoUseCase;
import com.example.clinica_medica.application.port.in.PacienteUseCase;
import com.example.clinica_medica.domain.model.Consulta;
import com.example.clinica_medica.generated.web.WebConsultaApi;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebConsultaController implements WebConsultaApi {

  private final ConsultaUseCase consultaUseCase;

  private final PacienteUseCase pacienteUseCase;

  private final MedicoUseCase medicoUseCase;

  public WebConsultaController(
      ConsultaUseCase consultaUseCase,
      PacienteUseCase pacienteUseCase,
      MedicoUseCase medicoUseCase) {
    this.consultaUseCase = consultaUseCase;
    this.pacienteUseCase = pacienteUseCase;
    this.medicoUseCase = medicoUseCase;
  }

  @Override
  public String listarConsultas(Model model) {
    model.addAttribute("consultas", consultaUseCase.listarTodasConsultas());
    return "consultas/lista";
  }

  @Override
  public String formNovaConsulta(Model model) {
    model.addAttribute("consulta", new Consulta());
    model.addAttribute("pacientes", pacienteUseCase.listarTodosPacientes());
    model.addAttribute("medicos", medicoUseCase.listarTodosMedicos());
    return "consultas/form";
  }

  @Override
  public String salvarConsulta(
      @Valid @ModelAttribute("consulta") Consulta consulta,
      BindingResult result,
      RedirectAttributes attributes,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("pacientes", pacienteUseCase.listarTodosPacientes());
      model.addAttribute("medicos", medicoUseCase.listarTodosMedicos());
      return "consultas/form";
    }

    try {
      if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
        consulta.setPaciente(pacienteUseCase.buscarPacientePorId(consulta.getPaciente().getId()));
      }
      if (consulta.getMedico() != null && consulta.getMedico().getId() != null) {
        consulta.setMedico(medicoUseCase.buscarMedicoPorId(consulta.getMedico().getId()));
      }

      if (consulta.getPaciente() == null || consulta.getMedico() == null) {
        throw new IllegalArgumentException("Paciente e médico devem ser informados");
      }

      if (consulta.getId() == null) {
        consultaUseCase.agendarConsulta(consulta);
        attributes.addFlashAttribute("mensagem", "Consulta agendada com sucesso!");
      } else {
        consultaUseCase.atualizarConsulta(consulta.getId(), consulta);
        attributes.addFlashAttribute("mensagem", "Consulta atualizada com sucesso!");
      }
      return "redirect:/consultas";
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao agendar consulta: " + e.getMessage());
      return consulta.getId() == null
          ? "redirect:/consultas/nova"
          : "redirect:/consultas/editar/" + consulta.getId();
    }
  }

  @Override
  public String formEditarConsulta(@PathVariable String id, Model model) {
    Consulta consulta = consultaUseCase.buscarConsultaPorId(id);
    if (consulta == null) {
      return "redirect:/consultas";
    }
    model.addAttribute("consulta", consulta);
    model.addAttribute("pacientes", pacienteUseCase.listarTodosPacientes());
    model.addAttribute("medicos", medicoUseCase.listarTodosMedicos());
    return "consultas/form";
  }

  @Override
  public String excluirConsulta(@PathVariable String id, RedirectAttributes attributes) {
    try {
      consultaUseCase.excluirConsulta(id);
      attributes.addFlashAttribute("mensagem", "Consulta excluída com sucesso!");
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao excluir consulta: " + e.getMessage());
    }
    return "redirect:/consultas";
  }
}
