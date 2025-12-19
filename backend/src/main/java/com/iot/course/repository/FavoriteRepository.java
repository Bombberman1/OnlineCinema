package com.iot.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    List<Favorite> findByUserId(Long userId);
}
