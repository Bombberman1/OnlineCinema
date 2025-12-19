package com.iot.course.dto.auth;

public record SignUpRequestDTO(
    String email,
    String password,
    boolean admin
) {}
