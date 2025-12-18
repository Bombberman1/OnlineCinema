package com.iot.course.dto.favorite;

public record FavoriteResponseDTO(
    Long movieId,
    String title,
    String posterUrl
) {}
