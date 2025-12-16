package com.iot.course.service;

import com.iot.course.dto.auth.AuthResponseDTO;
import com.iot.course.exception.Exists;
import com.iot.course.model.User;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;

    public void signup(String email, String password, boolean isAdmin) {
        if (userRepository.existsByEmail(email)) {
            throw new Exists("User already exists");
        }

        User user = User.builder()
                        .email(email)
                        .passwordHash(encoder.encode(password))
                        .isAdmin(isAdmin)
                        .build();

        userRepository.save(user);
    }

    public AuthResponseDTO login(String email, String password) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }
}
