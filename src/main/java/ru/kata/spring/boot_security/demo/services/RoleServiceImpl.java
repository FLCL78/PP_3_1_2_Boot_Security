package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

@Component
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
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
    @Transactional
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    @Transactional
    public Role findById(Long id) {
        return roleRepository.getById(id);
    }


}
