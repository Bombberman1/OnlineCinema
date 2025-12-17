package com.iot.course.dto.auth;

public record LogInRequestDTO(
    String email,
    String password
) {}
