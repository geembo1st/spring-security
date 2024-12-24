package ru.kata.spring.boot_security.demo.util;

public class PersonNotCreateException extends RuntimeException {
    public PersonNotCreateException(String mes) {
        super(mes);
    }
}
