package com.example.clinica_medica.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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
  public Object handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
    if (request.getRequestURI().startsWith("/api")) {
      if (ex.getMessage().contains("não encontrado")) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    String mensagemErro;
    if (ex instanceof DataIntegrityViolationException
        || ex.getCause() instanceof ConstraintViolationException) {
      mensagemErro = "Não é possível excluir este registro porque está sendo usado em outro local.";
    } else {
      mensagemErro = ex.getMessage();
    }

    return "redirect:" + request.getHeader("Referer") + "?error=" + mensagemErro;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
