package br.com.autoinsight.autoinsight_client.modules.auth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String loginPage(
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      Model model) {

    if (error != null) {
      model.addAttribute("error", "Email ou senha inválidos. Tente novamente.");
    }

    if (logout != null) {
      model.addAttribute("successMessage", "Logout realizado com sucesso!");
    }

    return "login";
  }
}
