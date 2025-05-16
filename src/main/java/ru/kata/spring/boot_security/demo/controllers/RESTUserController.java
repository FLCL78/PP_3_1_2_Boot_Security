package ru.kata.spring.boot_security.demo.controllers;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;


@RestController
@RequestMapping("/api/user")
public class RESTUserController {


    @GetMapping("/{id}")
    public ResponseEntity<?> showUser(@PathVariable("id") Long id, Authentication authentication) {
        //Добываем юзера аутентифицированного
        User currentUser = (User) authentication.getPrincipal();

        //Проверка на админность
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        //Только на своей странице будь, а админу можно всё.
        if(!isAdmin && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Доступ не доступ");
        }

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }
}

