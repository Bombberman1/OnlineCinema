package com.iot.course.dto.video_file;

public record VideoFileRequestDTO(
    Long movieId,
    Integer quality,
    String filePath
) {}
