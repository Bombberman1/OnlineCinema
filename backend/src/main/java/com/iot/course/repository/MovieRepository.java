package com.iot.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
