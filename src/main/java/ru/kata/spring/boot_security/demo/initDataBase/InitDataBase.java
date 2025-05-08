package ru.kata.spring.boot_security.demo.initDataBase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;


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
        //Записали в базу
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role roleUser = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        //Записали в базу
        User adminUser = new User("admin", "Василий", "Долговязый", 55, "admin", new HashSet<>());
        User adminOnly = new User("adminOnly", "Ахтубей", "Креплидзе", 22, "adminOnly", new HashSet<>());
        User user = new User("user", "user", "userevich", 33, "user", new HashSet<>());

        userRepository.save(adminUser);
        userRepository.save(adminOnly);
        userRepository.save(user);

        //Связь
        roleAdmin.addUser(adminUser);
        roleAdmin.addUser(adminOnly);
        roleUser.addUser(adminUser);
        roleUser.addUser(user);

        //Подтверждение связи с другой стороны
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

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
