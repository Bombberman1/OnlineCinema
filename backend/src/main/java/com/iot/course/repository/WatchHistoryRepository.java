package com.iot.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId);
    List<WatchHistory> findTop20ByUserIdOrderByWatchedAtDesc(Long userId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
