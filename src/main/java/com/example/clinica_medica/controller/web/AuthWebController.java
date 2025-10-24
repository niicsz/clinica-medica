package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.services.AuthService;
import com.example.clinica_medica.services.AuthService.AuthResult;
import com.example.clinica_medica.services.AuthService.UsuarioResumo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthWebController {

  @Autowired private AuthService authService;

  @GetMapping("/login")
  public String loginForm(Model model) {
    if (!model.containsAttribute("loginForm")) {
      model.addAttribute("loginForm", new LoginForm());
    }
    return "auth/login";
  }

  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute("loginForm") LoginForm form,
      BindingResult result,
      HttpServletResponse response,
      RedirectAttributes attributes) {
    if (result.hasErrors()) {
      return "auth/login";
    }

    try {
      AuthResult authResult = authService.authenticate(form.getEmail(), form.getSenha());
      response.addHeader(HttpHeaders.SET_COOKIE, authResult.cookie().toString());
      UsuarioResumo user = authResult.response().user();
      attributes.addFlashAttribute("mensagem", "Bem-vindo, " + user.nome() + "!");
      return "redirect:/";
    } catch (BadCredentialsException ex) {
      result.reject("credenciais.invalidas", "Credenciais inválidas. Verifique seus dados e tente novamente.");
      return "auth/login";
    }
  }

  @PostMapping("/logout")
  public String logout(HttpServletResponse response, RedirectAttributes attributes) {
    response.addHeader(HttpHeaders.SET_COOKIE, authService.logoutCookie().toString());
    attributes.addFlashAttribute("mensagem", "Sessão encerrada com sucesso.");
    return "redirect:/auth/login";
  }

  public static class LoginForm {
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória") private String senha;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getSenha() {
      return senha;
    }

    public void setSenha(String senha) {
      this.senha = senha;
    }
  }
}
