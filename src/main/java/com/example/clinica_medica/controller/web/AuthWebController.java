package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.application.dto.AuthResult;
import com.example.clinica_medica.application.dto.RegistrationData;
import com.example.clinica_medica.application.port.in.AuthUseCase;
import com.example.clinica_medica.controller.web.form.LoginForm;
import com.example.clinica_medica.controller.web.form.RegisterForm;
import com.example.clinica_medica.generated.web.AuthWebApi;
import com.example.clinica_medica.security.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthWebController implements AuthWebApi {

  private final AuthUseCase authUseCase;

  public AuthWebController(AuthUseCase authUseCase) {
    this.authUseCase = authUseCase;
  }

  @Override
  public UserRole[] rolesDisponiveis() {
    return UserRole.values();
  }

  @Override
  public String loginForm(Model model) {
    ensureLoginForm(model);
    if (!model.containsAttribute("registerForm")) {
      model.addAttribute("registerForm", new RegisterForm());
    }
    return "auth/login";
  }

  @Override
  public String login(
      @Valid @ModelAttribute("loginForm") LoginForm form,
      BindingResult result,
      HttpServletResponse response,
      RedirectAttributes attributes,
      Model model) {
    if (result.hasErrors()) {
      if (!model.containsAttribute("registerForm")) {
        model.addAttribute("registerForm", new RegisterForm());
      }
      return "auth/login";
    }

    try {
      AuthResult authResult = authUseCase.authenticate(form.getEmail(), form.getSenha());
      response.addHeader(
          org.springframework.http.HttpHeaders.SET_COOKIE, authResult.getCookie().toString());
      attributes.addFlashAttribute(
          "mensagem", "Bem-vindo, " + authResult.getResponse().getUser().getNome() + "!");
      return "redirect:/";
    } catch (BadCredentialsException ex) {
      result.reject(
          "credenciais.invalidas",
          "Credenciais inválidas. Verifique seus dados e tente novamente.");
      if (!model.containsAttribute("registerForm")) {
        model.addAttribute("registerForm", new RegisterForm());
      }
      return "auth/login";
    }
  }

  @Override
  public String register(
      @Valid @ModelAttribute("registerForm") RegisterForm form,
      BindingResult result,
      HttpServletResponse response,
      Model model,
      RedirectAttributes attributes) {
    if (result.hasErrors()) {
      ensureLoginForm(model);
      return "auth/login";
    }

    try {
      Set<UserRole> roles = form.getRoles() == null ? Set.of() : new HashSet<>(form.getRoles());
      RegistrationData registrationData =
          new RegistrationData(
              form.getNome(),
              form.getCpf(),
              form.getIdade(),
              form.getEmail(),
              form.getSenha(),
              roles);

      AuthResult authResult = authUseCase.register(registrationData);
      response.addHeader(
          org.springframework.http.HttpHeaders.SET_COOKIE, authResult.getCookie().toString());
      attributes.addFlashAttribute(
          "mensagem",
          "Conta criada com sucesso! Bem-vindo, "
              + authResult.getResponse().getUser().getNome()
              + "!");
      return "redirect:/";
    } catch (DuplicateKeyException ex) {
      result.reject("usuario.duplicado", "Já existe um usuário com o mesmo CPF ou e-mail.");
    } catch (DataIntegrityViolationException ex) {
      result.reject(
          "usuario.invalido", "Não foi possível criar o usuário. Verifique os dados informados.");
    } catch (IllegalArgumentException ex) {
      result.reject("usuario.invalido", ex.getMessage());
    }

    ensureLoginForm(model);
    return "auth/login";
  }

  @Override
  public String logout(HttpServletResponse response, RedirectAttributes attributes) {
    response.addHeader(
        org.springframework.http.HttpHeaders.SET_COOKIE,
        authUseCase.logoutCookie().toString());
    attributes.addFlashAttribute("mensagem", "Sessão encerrada com sucesso.");
    return "redirect:/auth/login";
  }

  private void ensureLoginForm(Model model) {
    if (!model.containsAttribute("loginForm")) {
      model.addAttribute("loginForm", new LoginForm());
    }
  }
}
