package com.iot.course.dto.report;

public record PopularMovieDTO(
    Long movieId,
    String title,
    Long viewCount
) {}

