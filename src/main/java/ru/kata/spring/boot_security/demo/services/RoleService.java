package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Optional;

@Component
public class RoleService {

    private final RoleRepository roleRepository;


    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;

    }
    @Transactional(readOnly = true)
    public Optional<Role> findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

}
