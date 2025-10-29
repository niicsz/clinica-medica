package com.example.clinica_medica.generated.web;

import com.example.clinica_medica.domain.model.Usuario;
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
@RequestMapping("/usuarios")
public interface WebUsuarioApi {

  @GetMapping
  String listarUsuarios(Model model);

  @GetMapping("/novo")
  String formNovoUsuario(Model model);

  @PostMapping("/salvar")
  String salvarUsuario(
      @Valid @ModelAttribute("usuario") Usuario usuario,
      BindingResult result,
      RedirectAttributes attributes,
      Model model);

  @GetMapping("/editar/{id}")
  String formEditarUsuario(@PathVariable("id") String id, Model model);

  @GetMapping("/excluir/{id}")
  String excluirUsuario(@PathVariable("id") String id, RedirectAttributes attributes);
}
