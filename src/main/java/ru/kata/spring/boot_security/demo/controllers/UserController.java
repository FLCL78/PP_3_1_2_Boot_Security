package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.ServiceBase;

import java.nio.file.AccessDeniedException;


@Controller
@RequestMapping("/user")
public class UserController {


    private final ServiceBase serviceBase;
    @Autowired
    public UserController(ServiceBase serviceBase) {
        this.serviceBase = serviceBase;
    }

    @GetMapping("/{id}")
   public String showUser(@PathVariable("id") Long id, Model model, Authentication authentication) throws AccessDeniedException {
        //Добываем юзера аутентифицированного
        User currentUser = (User) authentication.getPrincipal();

        //Проверка на админность
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        //Только на своей странице будь, а админу можно всё.
        if(!isAdmin && !currentUser.getId().equals(id)) {
            return "redirect:/user/" + currentUser.getId();
        }
        model.addAttribute("user", serviceBase.show(id));
    return "users/user";
   }


}
