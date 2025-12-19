package com.iot.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.course.model.Genre;
import com.iot.course.repository.GenreRepository;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    public Genre getOrCreate(String name) {
        return genreRepository.findByName(name)
                            .orElseGet(() -> genreRepository.save(
                                Genre.builder().name(name).build()
                            ));
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}

