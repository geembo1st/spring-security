package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    @NotEmpty(message = "Заполните поле")
    private String name;

    @NotEmpty(message = "Заполните поле")
    private String username;

    @NotEmpty(message = "Заполните поле")
    private String password;

    @NotEmpty(message = "Заполните поле")
    @Email
    private String email;

    @NotEmpty(message = "Заполните поле")
    @Min(value = 0, message = "Возраст должен быть больше 0")
    private Integer age;

    @NotEmpty(message = "Роли не могут быть пустыми")
    private Set<String> roles;

    public UserDTO() {}
    public UserDTO(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.roles = user.getRoles().stream()
                                    .map(Role::getName)
                                    .collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
