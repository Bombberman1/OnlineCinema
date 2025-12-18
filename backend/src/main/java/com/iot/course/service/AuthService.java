package com.iot.course.service;

import com.iot.course.dto.auth.ChangePasswordDTO;
import com.iot.course.dto.auth.LogInRequestDTO;
import com.iot.course.dto.auth.LogInResponseDTO;
import com.iot.course.dto.auth.SignUpRequestDTO;
import com.iot.course.exception.AuthFailed;
import com.iot.course.exception.Exists;
import com.iot.course.exception.InvalidData;
import com.iot.course.exception.NotFound;
import com.iot.course.model.User;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void signup(SignUpRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new Exists("User already exists");
        }

        User user = User.builder()
                        .email(dto.email())
                        .passwordHash(encoder.encode(dto.password()))
                        .isAdmin(dto.admin())
                        .build();

        userRepository.save(user);
    }

    public LogInResponseDTO login(LogInRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return new LogInResponseDTO(token);

        } catch (BadCredentialsException ex) {
            throw new AuthFailed("Invalid email or password");
        } catch (InternalAuthenticationServiceException ex) {
            throw new AuthFailed("Invalid email or password");
        } catch (AuthenticationException ex) {
            throw new AuthFailed("Authentication failed");
        }
    }

    public void changePassword(ChangePasswordDTO dto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                                .orElseThrow(() -> new NotFound("User not found"));

        if (!encoder.matches(dto.oldPassword(), user.getPasswordHash())) {
            throw new InvalidData("Old password is incorrect");
        }

        user.setPasswordHash(encoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
}
