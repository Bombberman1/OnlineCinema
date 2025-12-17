package com.iot.course.dto.profile;

import java.time.LocalDate;

public record ProfileResponseDTO(
    String username,
    String avatarUrl,
    LocalDate birthDate,
    String country
) {}
