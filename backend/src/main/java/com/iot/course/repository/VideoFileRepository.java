package com.iot.course.repository;

import com.iot.course.model.VideoFile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {
    Optional<VideoFile> findByMovieIdAndQuality(Long movieId, Integer quality);

    List<VideoFile> findByMovieId(Long movieId);

    // boolean existsByMovieIdAndQuality(Long movieId, Integer quality);
}
