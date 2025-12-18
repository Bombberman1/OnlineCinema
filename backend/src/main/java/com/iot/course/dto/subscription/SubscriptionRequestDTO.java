package com.iot.course.dto.subscription;

import java.math.BigDecimal;

public record SubscriptionRequestDTO(
    BigDecimal price,
    int days
) {}
