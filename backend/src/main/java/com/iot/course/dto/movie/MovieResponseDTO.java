package com.iot.course.dto.movie;

import java.math.BigDecimal;
import java.util.List;

public record MovieResponseDTO(
    Long id,
    String title,
    String description,
    int releaseYear,
    String country,
    BigDecimal rating,
    String posterUrl,
    List<String> genres
) {}
