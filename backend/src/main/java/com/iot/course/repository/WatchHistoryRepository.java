package com.iot.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
}
