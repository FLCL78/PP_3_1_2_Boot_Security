package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping()
    public String index(Model model, @AuthenticationPrincipal User principal) {
        model.addAttribute("user", principal);
        model.addAttribute("users", userService.index());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("newUser", new User());
        return "admin/admin";   //admin view
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/user_form";
    }

    @PostMapping("/new")
    public String save(@ModelAttribute("newUser") User user) {
        Set<Role> selectedRoles = user.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        user.setRoles(selectedRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin#users";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.show(id));
        return "admin/edit";  //edit view
    }
    @PostMapping("/update")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        Set<Role> selectedRoles = user.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        user.setRoles(selectedRoles);
        User existUser = userService.show(id);
        if(user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.update(id, user);
        return "redirect:/admin"; //функционал изменения, сначала правим в модель выше, затем обновляем объект.
        }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }


}
