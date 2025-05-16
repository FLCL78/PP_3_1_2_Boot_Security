package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageForwarder {

    @GetMapping("/login")
    public String getLoginPage() {
        return "forward:/login/index.html";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "forward:/admin/index.html";
    }

    @GetMapping("/user")
    public String forwardUserRoot() {
        return "forward:/user/index.html";
    }

    @GetMapping("/user/{path:^(?!index\\.html$).*$}")
    public String forwardUserSubPath() {
        return "forward:/user/index.html";
    }
}