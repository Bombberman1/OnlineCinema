package com.iot.course.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
