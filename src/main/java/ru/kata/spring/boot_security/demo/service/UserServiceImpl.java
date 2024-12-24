package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepositories;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundException;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final PeopleRepositories peopleRepositories;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PeopleRepositories peopleRepositories, PasswordEncoder passwordEncoder) {
        this.peopleRepositories = peopleRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepositories.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> showPeople() {
        return peopleRepositories.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User showUserById(Long id) {
        Optional<User> foundUser = peopleRepositories.findById(id);
        return foundUser.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        peopleRepositories.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, User updateUser) {
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        peopleRepositories.save(updateUser);
    }

    @Override
    public User getUserByUsername(String username) {
        return peopleRepositories.findByUsername(username);
    }
}
