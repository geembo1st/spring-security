package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepositories;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepositories roleRepositories;


    @Autowired
    public void setRole(RoleRepositories roleRepositories) {
        this.roleRepositories = roleRepositories;
    }

    @Override
    public Role findByName(String name) {
        return roleRepositories.findByName(name);
    }

    @Override
    public void saveRole(Role role) {
        roleRepositories.save(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepositories.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepositories.getById(id);
    }
}
