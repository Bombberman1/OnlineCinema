package com.iot.course.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iot.course.model.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId);
    List<WatchHistory> findTop20ByUserIdOrderByWatchedAtDesc(Long userId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT COUNT(DISTINCT wh.user.id) FROM WatchHistory wh WHERE wh.watchedAt >= :since")
    Long countDistinctUsersSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(wh) FROM WatchHistory wh WHERE wh.watchedAt >= :since")
    Long countViewsSince(@Param("since") LocalDateTime since);

    @Query("SELECT wh.movie.id, COUNT(wh) as viewCount FROM WatchHistory wh GROUP BY wh.movie.id ORDER BY viewCount DESC")
    List<Object[]> findMovieViewCounts();

    @Query("SELECT g.id, g.name, COUNT(wh) as viewCount " +
           "FROM WatchHistory wh " +
           "JOIN wh.movie m " +
           "JOIN m.genres g " +
           "GROUP BY g.id, g.name " +
           "ORDER BY viewCount DESC")
    List<Object[]> findGenreViewCounts();
}
