package com.iot.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.auth.AuthResponseDTO;
import com.iot.course.dto.auth.LogInRequestDTO;
import com.iot.course.dto.auth.SignUpRequestDTO;
import com.iot.course.dto.error.ErrorResponseDTO;
import com.iot.course.exception.InvalidData;
import com.iot.course.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDTO request) {
        authService.signup(request.getEmail(), request.getPassword(), request.isAdmin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LogInRequestDTO request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @ExceptionHandler(InvalidData.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(InvalidData ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // @ExceptionHandler(UnauthorizedException.class)
    // public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
    //     return ResponseEntity
    //             .status(HttpStatus.UNAUTHORIZED)
    //             .body(new ErrorResponse(ex.getMessage()));
    // }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponseDTO("Internal server error"));
    }
}
