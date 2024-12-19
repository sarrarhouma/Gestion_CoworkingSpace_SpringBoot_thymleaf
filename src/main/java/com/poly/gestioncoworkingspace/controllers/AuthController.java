package com.poly.gestioncoworkingspace.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String goToHomePage() {
        return "redirect:/home"; // Redirection vers la page d'accueil
    }

    @GetMapping("/erreur")
    public String goToErrorPage() {
        return "errorPage"; // Page d'erreur personnalisée
    }
}
