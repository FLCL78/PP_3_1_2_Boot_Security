package ru.kata.spring.boot_security.demo.initDataBase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
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

        //Создали и наладили связь
        User adminUser = new User("admin", "Василий", "Долговязый", 55, "admin", Set.of(roleAdmin, roleUser));
        User adminOnly = new User("adminOnly", "Ахтубей", "Креплидзе", 22, "adminOnly",Set.of(roleAdmin));
        User user = new User("user", "user", "userevich", 33, "user", Set.of(roleUser));

        //Закодировали парольчики
        String encodedPassAdmin = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encodedPassAdmin);
        String encodedPassAdminOnly = passwordEncoder.encode(adminOnly.getPassword());
        adminOnly.setPassword(encodedPassAdminOnly);
        String encodedPassUser = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassUser);

        //Сохранили в базу со всеми обновлениями
        userRepository.save(adminUser);
        userRepository.save(adminOnly);
        userRepository.save(user);

    }

//    @PreDestroy
//    public void deleteFromBase() {
//        User admin = userRepository.findByUsername("admin").orElseThrow();
//        userRepository.delete(admin);
//
//        User adminOnly = userRepository.findByUsername("adminOnly").orElseThrow();
//        userRepository.delete(adminOnly);
//
//        User user = userRepository.findByUsername("user").orElseThrow();
//        userRepository.delete(user);
//
//
//    }
}
