package com.iot.course.dto.subscription;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponseDTO(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal price
) {}
