package com.iot.course.dto.report;

public record PopularGenreDTO(
    Long genreId,
    String genreName,
    Long viewCount
) {}

