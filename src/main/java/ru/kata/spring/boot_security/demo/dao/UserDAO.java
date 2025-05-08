package ru.kata.spring.boot_security.demo.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;


@Repository
public class UserDAO implements DAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> index() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User show(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void save(User user) {

        entityManager.persist(user);
    }

    @Override
    public void update(Long id, User updatedUser) {
        User user = entityManager.find(User.class, id);
        user.setAge(updatedUser.getAge());
        user.setLastName(updatedUser.getLastName());
        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }
}
