package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;




@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }


    @GetMapping()
    public String index(Model model, @AuthenticationPrincipal User principal) {
        model.addAttribute("user", principal);
        model.addAttribute("users", userService.index());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/admin";   //admin view
    }

    @PostMapping("/new")
    public String save(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin#users";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        userService.update(id, user);
        return "redirect:/admin"; //функционал изменения, сначала правим в модель выше, затем обновляем объект.
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }


}
