package com.iot.course.dto.video_file;

public record VideoFileUploadRequestDTO(
    Long movieId,
    Integer quality
) {}
