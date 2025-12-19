package com.iot.course.dto.report;

import java.time.LocalDateTime;

public record UserActivityDTO(
    Long totalUsers,
    Long activeUsers,
    Long totalViews,
    LocalDateTime reportDate
) {}

