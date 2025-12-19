package com.iot.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.auth.LogInResponseDTO;
import com.iot.course.dto.auth.ChangePasswordDTO;
import com.iot.course.dto.auth.LogInRequestDTO;
import com.iot.course.dto.auth.SignUpRequestDTO;
import com.iot.course.dto.error.ErrorResponseDTO;
import com.iot.course.exception.AuthFailed;
import com.iot.course.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignUpRequestDTO dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDTO> login(@RequestBody LogInRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PutMapping("/change_password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO dto) {
        authService.changePassword(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(AuthFailed.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthFailed(AuthFailed ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }
}
