package com.iot.course.dto.auth;

public record ChangePasswordDTO(
        String oldPassword,
        String newPassword
) {}
