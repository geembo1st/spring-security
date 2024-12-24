package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.PersonErrorResponse;
import ru.kata.spring.boot_security.demo.util.PersonNotCreateException;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {
       if(bindingResult.hasErrors()) {
           StringBuilder mes = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                mes.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
            }
            throw new PersonNotCreateException(mes.toString());
       }
       userService.addUser(convertToUser(userDTO));
       return ResponseEntity.ok(HttpStatus.OK);
    }

    private User convertToUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleService::findByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userDTO.setRoles(roleNames);
        return userDTO;
    }

    @GetMapping("/find")
    public UserDTO showPersonById(@RequestParam(value = "id", required = true) Long id) {
        return convertToUserDTO(userService.showUserById(id));
    }

    @GetMapping("/people")
    public List<UserDTO> showPeople() {
        return userService.showPeople().stream()
                                        .map(this::convertToUserDTO)
                                        .collect(Collectors.toList());
    }

    @PatchMapping("/find/edit")
    public ResponseEntity<HttpStatus> update(@RequestParam("id") Long id,
                                             @RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new PersonNotCreateException(errorMessage.toString());
        }

        User existingUser = userService.showUserById(id);
        User user = convertToUser(userDTO);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.updateUser(id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/find/edit")
    public ResponseEntity<HttpStatus> delete(@RequestParam(value = "id", required = true) Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse("Person not found");
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreateException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(e.getMessage());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
