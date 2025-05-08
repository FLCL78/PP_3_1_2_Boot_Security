package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.DAO;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;


@Service
public class UserService implements ServiceBase{


    private final DAO userDao;


    @Autowired
    public UserService(DAO userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public List<User> index() {
        return userDao.index();
    }

    @Override
    @Transactional
    public User show(Long id) {
        return userDao.show(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void update(Long id, User updatedUser) {
        userDao.update(id,updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userDao.show(id);
        user.getRoles().forEach(role -> role.getUsers().remove(user));
        userDao.save(user);
        userDao.delete(id);
    }
}
