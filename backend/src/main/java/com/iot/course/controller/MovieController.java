package com.iot.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.movie.MovieRequestDTO;
import com.iot.course.dto.movie.MovieResponseDTO;
import com.iot.course.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MovieRequestDTO dto) {
        movieService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{id}")
    public MovieResponseDTO get(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @GetMapping
    public List<MovieResponseDTO> getAll() {
        return movieService.getAll();
    }

    @GetMapping("/recommends")
    public List<MovieResponseDTO> getAllRecommends() {
        return movieService.getAllRecommends();
    }
}
