package com.iot.course.service;

import com.iot.course.model.Profile;
import com.iot.course.model.User;
import com.iot.course.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                        .email(email)
                        .passwordHash(rawPassword)
                        .isAdmin(false)
                        .build();

        Profile profile = Profile.builder()
                                .user(user)
                                .build();

        user.setProfile(profile);

        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                            .orElse(null);
    }
}
