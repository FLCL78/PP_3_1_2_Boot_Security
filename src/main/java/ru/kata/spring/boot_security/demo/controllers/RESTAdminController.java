package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class RESTAdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public RESTAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.index());
    }
    @PostMapping("/new")
    public ResponseEntity<String> save(User user) {
        userService.save(user);
        return ResponseEntity.ok("Пользователь готовченко");
    }
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Long id, User user) {
        userService.update(id, user);
        return ResponseEntity.ok("Обновлён чётко");
    }
    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok("Удалён чётко");
    }
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.show(id));
    }
}
