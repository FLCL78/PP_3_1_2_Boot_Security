package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userDao;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;



    @Autowired
    public UserServiceImpl(UserRepository userDao, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    @Transactional
    public List<User> index() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public User show(Long id) {
        return userDao.getById(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        Set<Role> roles = user.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Transactional
    public void update(Long id, User updatedUser) {
        Set<Role> selectedRoles = updatedUser.getRoles().stream()
                .map(role -> roleService.findById(role.getId()))
                .collect(Collectors.toSet());
        updatedUser.setRoles(selectedRoles);
        User existUser = userDao.getById(id);
        if(updatedUser.getPassword() == null || updatedUser.getPassword().isBlank()) {
            updatedUser.setPassword(existUser.getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        updatedUser.setId(existUser.getId());
        userDao.save(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDao.deleteById(id);
    }


    @Override
    @Transactional
    public User findUserWithRolesById(Long id) {
        return userDao.findUserWithRolesById(id).orElseThrow(() -> new UsernameNotFoundException("Потеря потерь"));
    }
}
