package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    List<User> showPeople();
    User showUserById(Long id);
    void deleteUserById(Long id);
    void updateUser(Long id, User updateUser);
    User getUserByUsername(String username);
}
