package com.poly.gestioncoworkingspace.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {



        @GetMapping("/home")
        public String home() {
            return "home"; // Fichier templates/home.html
        }
    }

