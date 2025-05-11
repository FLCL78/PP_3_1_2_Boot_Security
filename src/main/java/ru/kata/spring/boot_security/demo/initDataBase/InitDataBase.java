package ru.kata.spring.boot_security.demo.initDataBase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class InitDataBase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public InitDataBase(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void userToBase() {
        //Записали в базу
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role roleUser = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        //Создали связь
        User adminUser = new User("Василий","Долговязый", 55, "admin@gmail.com", "admin", Set.of(roleAdmin, roleUser));
        User adminOnly = new User("Ахтубей", "Креплидзе", 22, "adminOnly@gmail.com", "adminOnly",Set.of(roleAdmin));
        User user = new User("User", "Userevich", 33, "user@gmail.com", "user", Set.of(roleUser));

        // Закодировали парольчики
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminOnly.setPassword(passwordEncoder.encode(adminOnly.getPassword()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Записали в базу
        userRepository.save(adminUser);
        userRepository.save(adminOnly);
        userRepository.save(user);

    }
    @PreDestroy
    public void destroy() {
        userRepository.deleteAll(userRepository.findAll());
    }

}
