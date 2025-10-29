package com.example.clinica_medica.controller.web;

import com.example.clinica_medica.generated.web.HomeWebApi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController implements HomeWebApi {

  @Override
  public String home(Model model) {
    model.addAttribute("title", "Clínica Médica");
    return "index";
  }
}
