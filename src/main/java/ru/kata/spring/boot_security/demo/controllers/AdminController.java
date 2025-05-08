package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.ServiceBase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final ServiceBase serviceBase;
    private final RoleService roleService;
    @Autowired
    public AdminController(ServiceBase serviceBase, RoleService roleService) {
        this.serviceBase = serviceBase;
        this.roleService = roleService;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", serviceBase.index());
        return "admin/admin";   //admin view
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/user_form";
    }

    @PostMapping()
    public String save(@ModelAttribute("user") User user) {
        Set<Role> selectedRoles = user.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        user.setRoles(selectedRoles);
        serviceBase.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", serviceBase.show(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit";  //edit view
    }
    @PostMapping("/update")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        Set<Role> selectedRoles = user.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        user.setRoles(selectedRoles);
        serviceBase.update(id, user);
        return "redirect:/admin"; //функционал изменения, сначала правим в модель выше, затем обновляем объект.
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        serviceBase.delete(id);
        return "redirect:/admin";
    }


}
