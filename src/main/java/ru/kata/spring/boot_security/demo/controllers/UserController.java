package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.services.ServiceBase;


@Controller
@RequestMapping("/user")
public class UserController {


    private final ServiceBase serviceBase;
    @Autowired
    public UserController(ServiceBase serviceBase) {
        this.serviceBase = serviceBase;
    }

    @GetMapping("/{id}")
   public String showUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", serviceBase.show(id));
    return "users/user";
   }


}
