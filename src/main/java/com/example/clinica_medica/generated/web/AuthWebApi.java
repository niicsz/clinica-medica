package com.example.clinica_medica.generated.web;

import com.example.clinica_medica.controller.web.form.LoginForm;
import com.example.clinica_medica.controller.web.form.RegisterForm;
import com.example.clinica_medica.security.UserRole;
import jakarta.annotation.Generated;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Generated(value = "swagger-codegen", date = "2024-10-24")
@RequestMapping("/auth")
public interface AuthWebApi {

  @ModelAttribute("rolesDisponiveis")
  UserRole[] rolesDisponiveis();

  @GetMapping("/login")
  String loginForm(Model model);

  @PostMapping("/login")
  String login(
      @Valid @ModelAttribute("loginForm") LoginForm form,
      BindingResult result,
      HttpServletResponse response,
      RedirectAttributes attributes,
      Model model);

  @PostMapping("/register")
  String register(
      @Valid @ModelAttribute("registerForm") RegisterForm form,
      BindingResult result,
      HttpServletResponse response,
      Model model,
      RedirectAttributes attributes);

  @PostMapping("/logout")
  String logout(HttpServletResponse response, RedirectAttributes attributes);
}
