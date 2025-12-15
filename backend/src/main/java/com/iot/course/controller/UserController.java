package com.iot.course.controller;

import com.iot.course.model.User;
import com.iot.course.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestParam String email, @RequestParam String password) {
        return userService.register(email, password);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return user;
    }
}
