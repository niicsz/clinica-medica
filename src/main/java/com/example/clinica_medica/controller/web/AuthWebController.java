package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.security.UserRole;
import com.example.clinica_medica.services.AuthResult;
import com.example.clinica_medica.services.AuthService;
import com.example.clinica_medica.services.RegistrationData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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

  @ModelAttribute("rolesDisponiveis")
  public UserRole[] rolesDisponiveis() {
    return UserRole.values();
  }

  @GetMapping("/login")
  public String loginForm(Model model) {
    ensureLoginForm(model);
    if (!model.containsAttribute("registerForm")) {
      model.addAttribute("registerForm", new RegisterForm());
    }
    return "auth/login";
  }

  @PostMapping("/login")
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
      AuthResult authResult = authService.authenticate(form.getEmail(), form.getSenha());
      response.addHeader(HttpHeaders.SET_COOKIE, authResult.getCookie().toString());
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

  @PostMapping("/register")
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

      AuthResult authResult = authService.register(registrationData);
      response.addHeader(HttpHeaders.SET_COOKIE, authResult.getCookie().toString());
      attributes.addFlashAttribute(
          "mensagem", "Conta criada com sucesso! Bem-vindo, " + authResult.getResponse().getUser().getNome() + "!");
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

  @PostMapping("/logout")
  public String logout(HttpServletResponse response, RedirectAttributes attributes) {
    response.addHeader(HttpHeaders.SET_COOKIE, authService.logoutCookie().toString());
    attributes.addFlashAttribute("mensagem", "Sessão encerrada com sucesso.");
    return "redirect:/auth/login";
  }

  private void ensureLoginForm(Model model) {
    if (!model.containsAttribute("loginForm")) {
      model.addAttribute("loginForm", new LoginForm());
    }
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

  public static class RegisterForm {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade deve ser positiva")
    private Integer idade;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotEmpty(message = "Selecione ao menos um perfil")
    private Set<UserRole> roles = new HashSet<>();

    public String getNome() {
      return nome;
    }

    public void setNome(String nome) {
      this.nome = nome;
    }

    public String getCpf() {
      return cpf;
    }

    public void setCpf(String cpf) {
      this.cpf = cpf;
    }

    public Integer getIdade() {
      return idade;
    }

    public void setIdade(Integer idade) {
      this.idade = idade;
    }

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

    public Set<UserRole> getRoles() {
      return roles;
    }

    public void setRoles(Set<UserRole> roles) {
      this.roles = roles;
    }
  }
}
