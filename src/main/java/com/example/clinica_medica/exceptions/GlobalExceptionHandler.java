package com.example.clinica_medica.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public Object handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes attributes) {
    if (request.getRequestURI().startsWith("/api")) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
    attributes.addFlashAttribute("mensagemErro", ex.getMessage());
    String referer = request.getHeader("Referer");
    return "redirect:" + (referer != null ? referer : "/");
  }

  @ExceptionHandler(
      value = {DataIntegrityViolationException.class, ConstraintViolationException.class})
  public ResponseEntity<Object> handleDataIntegrityViolation(
      Exception ex, HttpServletRequest request) {
    if (request.getRequestURI().startsWith("/api")) {
      String message =
          "Não é possível excluir este registro porque está sendo usado em outro local.";
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    throw new RuntimeException(ex);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public String handleRuntimeException(
      RuntimeException ex, RedirectAttributes attributes, HttpServletRequest request) {
    if (!request.getRequestURI().startsWith("/api")) {
      String mensagemErro;

      if (ex instanceof DataIntegrityViolationException
          || ex.getCause() instanceof ConstraintViolationException) {
        mensagemErro =
            "Não é possível excluir este registro porque está sendo usado em outro local.";
      } else {
        mensagemErro = ex.getMessage();
      }

      attributes.addFlashAttribute("mensagemErro", mensagemErro);

      String url = request.getRequestURI();
      if (url.contains("/medicos")) {
        return "redirect:/medicos";
      } else if (url.contains("/pacientes")) {
        return "redirect:/pacientes";
      } else if (url.contains("/consultas")) {
        return "redirect:/consultas";
      } else if (url.contains("/usuarios")) {
        return "redirect:/usuarios";
      } else {
        return "redirect:/";
      }
    }

    throw ex;
  }
}
