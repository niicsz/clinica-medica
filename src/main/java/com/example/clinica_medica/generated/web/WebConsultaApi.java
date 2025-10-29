package com.example.clinica_medica.generated.web;

import com.example.clinica_medica.domain.model.Consulta;
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
@RequestMapping("/consultas")
public interface WebConsultaApi {

  @GetMapping
  String listarConsultas(Model model);

  @GetMapping("/nova")
  String formNovaConsulta(Model model);

  @PostMapping("/salvar")
  String salvarConsulta(
      @Valid @ModelAttribute("consulta") Consulta consulta,
      BindingResult result,
      RedirectAttributes attributes,
      Model model);

  @GetMapping("/editar/{id}")
  String formEditarConsulta(@PathVariable("id") String id, Model model);

  @GetMapping("/excluir/{id}")
  String excluirConsulta(@PathVariable("id") String id, RedirectAttributes attributes);
}
