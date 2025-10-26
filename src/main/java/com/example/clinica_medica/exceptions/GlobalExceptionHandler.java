package com.example.clinica_medica.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public Object handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes attributes) {
    logger.warn(
        "IllegalArgumentException capturada: {} - URI: {}",
        ex.getMessage(),
        request.getRequestURI());

    if (request.getRequestURI().startsWith("/api")) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
    attributes.addFlashAttribute("mensagemErro", ex.getMessage());
    String referer = request.getHeader("Referer");
    return "redirect:" + (referer != null ? referer : "/");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Object handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpServletRequest request,
      RedirectAttributes attributes) {
    logger.error(
        "HttpMessageNotReadableException capturada - URI: {} - Erro: {}",
        request.getRequestURI(),
        ex.getOriginalMessage());

    if (request.getRequestURI().startsWith("/api")) {
      String message =
          "Corpo da requisição inválido. Verifique o JSON enviado e tente novamente.";
      return ResponseEntity.badRequest().body(message);
    }

    attributes.addFlashAttribute(
        "mensagemErro",
        "Não foi possível processar os dados enviados. Verifique as informações e tente novamente.");
    String referer = request.getHeader("Referer");
    return "redirect:" + (referer != null ? referer : "/");
  }

  @ExceptionHandler(value = {DataIntegrityViolationException.class})
  public ResponseEntity<Object> handleDataIntegrityViolation(
      Exception ex, HttpServletRequest request) {
    logger.error(
        "DataIntegrityViolationException capturada - URI: {} - Erro: {}",
        request.getRequestURI(),
        ex.getMessage());

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
    logger.error(
        "RuntimeException capturada - URI: {} - Erro: {}",
        request.getRequestURI(),
        ex.getMessage(),
        ex);

    if (!request.getRequestURI().startsWith("/api")) {
      String mensagemErro;

      if (ex instanceof DataIntegrityViolationException
          || ex.getCause() instanceof DuplicateKeyException) {
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
