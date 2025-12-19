package com.iot.course.dto.watch_history;

import java.time.LocalDateTime;

public record WatchHistoryResponseDTO(
    Long movieId,
    LocalDateTime watchedAt
) {}
