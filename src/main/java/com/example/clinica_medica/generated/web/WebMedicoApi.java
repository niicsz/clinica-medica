package com.example.clinica_medica.generated.web;

import com.example.clinica_medica.domain.model.Medico;
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
@RequestMapping("/medicos")
public interface WebMedicoApi {

  @GetMapping
  String listarMedicos(Model model);

  @GetMapping("/novo")
  String formNovoMedico(Model model);

  @PostMapping("/salvar")
  String salvarMedico(
      @Valid @ModelAttribute("medico") Medico medico,
      BindingResult result,
      RedirectAttributes attributes);

  @GetMapping("/editar/{id}")
  String formEditarMedico(@PathVariable("id") String id, Model model);

  @GetMapping("/excluir/{id}")
  String excluirMedico(@PathVariable("id") String id, RedirectAttributes attributes);
}
