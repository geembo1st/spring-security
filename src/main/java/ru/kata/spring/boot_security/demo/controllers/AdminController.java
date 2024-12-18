package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/new")
    public String createUser(Model model) {
        List<Role> roleSet = roleService.getRoles();
        model.addAttribute("user", new User());
        model.addAttribute("role", roleSet);
        return "new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("testOrder") Set<String> roleSet) {
        Set<Role> roleHashSet = new HashSet<>();
        for (String roleStr : roleSet) {
            Role role = roleService.findByName(roleStr);
            roleHashSet.add(role);
        }
        user.setRoles(roleHashSet);
        userService.addUser(user);
        return "redirect:/admin/people";
    }

    @GetMapping("/find")
    public String showPersonById(@RequestParam(value = "id", required = true) Long id, Model model) {
        User user = userService.showUserById(id);
        model.addAttribute("user", user);
        return "people";
    }

    @GetMapping("/people")
    public String showPeople(Model model) {
        model.addAttribute("people", userService.showPeople());
        return "people";
    }

    @GetMapping("/find/edit")
    public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
        User user = userService.showUserById(id);
        List<Role> roles = roleService.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "edit";
    }

    @PatchMapping("/find/edit")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "id") Long id, @RequestParam("role") Set<String> roleSet) {
        User existingUser = userService.showUserById(id);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleSet) {
            Role role = roleService.findByName(roleName);
            roles.add(role);
        }
        user.setRoles(roles);
        userService.updateUser(user.getId(), user);
        return "redirect:/admin/people";
    }

    @DeleteMapping("/find/edit")
    public String delete(@ModelAttribute("user") User user, @RequestParam(value = "id", required = true) Long id) {
        userService.deleteUserById(user.getId());
        return "redirect:/admin/people";
    }
}
