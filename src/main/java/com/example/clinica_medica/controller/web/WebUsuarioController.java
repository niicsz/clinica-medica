package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.entities.Usuario;
import com.example.clinica_medica.security.UserRole;
import com.example.clinica_medica.services.UsuarioService;
import com.example.clinica_medica.utils.CPFUtils;
import com.example.clinica_medica.utils.EmailUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class WebUsuarioController {

  @Autowired private UsuarioService usuarioService;

  @GetMapping
  public String listarUsuarios(Model model) {
    model.addAttribute("usuarios", usuarioService.listarTodosUsuarios());
    model.addAttribute("rolesDisponiveis", UserRole.values());
    return "usuarios/lista";
  }

  @GetMapping("/novo")
  public String formNovoUsuario(Model model) {
    model.addAttribute("usuario", new Usuario());
    model.addAttribute("rolesDisponiveis", UserRole.values());
    return "usuarios/form";
  }

  @PostMapping("/salvar")
  public String salvarUsuario(
          @Valid @ModelAttribute("usuario") Usuario usuario,
          BindingResult result,
          RedirectAttributes attributes,
          Model model) {

    if (!CPFUtils.isCPFValido(usuario.getCpf())) {
      result.rejectValue("cpf", "error.usuario", "CPF inválido");
    }

    if (!EmailUtils.isEmailValido(usuario.getEmail())) {
      result.rejectValue("email", "error.usuario", "E-mail inválido");
    }

    if (result.hasErrors()) {
      model.addAttribute("rolesDisponiveis", UserRole.values());
      return "usuarios/form";
    }

    try {
      if (usuario.getId() == null) {
        usuarioService.incluirUsuario(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
      } else {
        usuarioService.atualizarUsuario(usuario.getId(), usuario);
        attributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
      }
      return "redirect:/usuarios";
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar usuário: " + e.getMessage());
      return usuario.getId() == null
          ? "redirect:/usuarios/novo"
          : "redirect:/usuarios/editar/" + usuario.getId();
    }
  }

  @GetMapping("/editar/{id}")
  public String formEditarUsuario(@PathVariable String id, Model model) {
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    if (usuario == null) {
      return "redirect:/usuarios";
    }
    usuario.setSenha(null);
    model.addAttribute("usuario", usuario);
    model.addAttribute("rolesDisponiveis", UserRole.values());
    return "usuarios/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluirUsuario(@PathVariable String id, RedirectAttributes attributes) {
    try {
      usuarioService.excluirUsuario(id);
      attributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
    } catch (Exception e) {
      attributes.addFlashAttribute("mensagemErro", "Erro ao excluir usuário: " + e.getMessage());
    }
    return "redirect:/usuarios";
  }
}
