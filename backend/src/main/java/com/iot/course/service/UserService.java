package com.iot.course.service;

import com.iot.course.dto.user.UserResponseDTO;
import com.iot.course.model.User;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        return new UserResponseDTO(
            userDetails.getUsername(),
            userDetails.isAdmin()
        );
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
