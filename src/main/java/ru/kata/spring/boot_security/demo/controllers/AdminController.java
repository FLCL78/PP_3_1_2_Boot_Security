package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.ServiceBase;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final ServiceBase serviceBase;
    @Autowired
    public AdminController(ServiceBase serviceBase) {
        this.serviceBase = serviceBase;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", serviceBase.index());
        return "admin/admin";   //admin view
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/user_form"; // new view
    }

    @PostMapping()
    public String save(@ModelAttribute("user") User user) {
        serviceBase.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", serviceBase.show(id));
        return "admin/edit";  //edit view
    }
    @PostMapping("/update")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        serviceBase.update(id, user);
        return "redirect:/admin"; //функционал изменения, сначала правим в модель выше, затем обновляем объект.
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        serviceBase.delete(id);
        return "redirect:/admin";
    }


}
