package com.iot.course.service;

import com.iot.course.model.User;
import com.iot.course.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.findById(id)
                            .orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
