package ru.kata.spring.boot_security.demo.initDataBase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class InitDataBase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public InitDataBase(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void userToBase() {
        User adminUser = new User("admin", "Василий", "Долговязый", 55, "admin", Set.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));
        User adminOnly = new User("adminOnly", "Ахтубей", "Креплидзе", 22, "adminOnly", Set.of(new Role("ROLE_ADMIN")));
        User user = new User("user", "user", "userevich", 33, "user", Set.of(new Role("ROLE_USER")));
        userRepository.save(adminUser);
        userRepository.save(adminOnly);
        userRepository.save(user);
    }

    @PreDestroy
    public void deleteFromBase() {
        User admin = userRepository.findByUsername("admin").orElseThrow();
        userRepository.delete(admin);

        User adminOnly = userRepository.findByUsername("adminOnly").orElseThrow();
        userRepository.delete(adminOnly);

        User user = userRepository.findByUsername("user").orElseThrow();
        userRepository.delete(user);


    }
}
