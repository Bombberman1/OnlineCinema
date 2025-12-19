package com.iot.course.dto.report;

import java.util.List;

public record ReportDTO(
    List<PopularMovieDTO> popularMovies,
    List<PopularGenreDTO> popularGenres,
    UserActivityDTO userActivity,
    SubscriptionRevenueDTO subscriptionRevenue
) {}

