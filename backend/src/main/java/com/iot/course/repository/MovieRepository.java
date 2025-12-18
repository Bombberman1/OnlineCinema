package com.iot.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByCountry(String country);

    List<Movie> findByReleaseYear(Integer releaseYear);
}
