package com.iot.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.favorite.FavoriteResponseDTO;
import com.iot.course.model.Favorite;
import com.iot.course.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{movieId}")
    public void create(@PathVariable Long movieId) {
        favoriteService.create(movieId);
    }

    @GetMapping("/current")
    public List<FavoriteResponseDTO> getCurrentFavorites() {
        return favoriteService.getUserFavorites();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Favorite> getAllFavorites() {
        return favoriteService.getAll();
    }
}
