package com.iot.course.dto.profile;

import java.time.LocalDate;

public record ProfileRequestDTO(
    String username,
    String avatarUrl,
    LocalDate birthDate,
    String country
) {}
