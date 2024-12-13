package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/user")
public class PeopleController {
    private final UserService userService;

    @Autowired
    public PeopleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "user";
    }

}
