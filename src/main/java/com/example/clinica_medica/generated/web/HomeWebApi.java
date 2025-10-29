package com.example.clinica_medica.generated.web;

import jakarta.annotation.Generated;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Generated(value = "swagger-codegen", date = "2024-10-24")
@RequestMapping("/")
public interface HomeWebApi {

  @GetMapping
  String home(Model model);
}
