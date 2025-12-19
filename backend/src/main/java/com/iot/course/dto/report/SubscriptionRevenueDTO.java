package com.iot.course.dto.report;

import java.math.BigDecimal;

public record SubscriptionRevenueDTO(
    Long totalSubscriptions,
    BigDecimal totalRevenue,
    BigDecimal averageRevenue
) {}

