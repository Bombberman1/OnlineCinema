package com.iot.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.iot.course.dto.favorite.FavoriteResponseDTO;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Favorite;
import com.iot.course.model.Movie;
import com.iot.course.model.User;
import com.iot.course.repository.FavoriteRepository;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    public void create(Long movieId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        if (favoriteRepository.existsByUserIdAndMovieId(userDetails.getId(), movieId)) return;

        User user = userRepository.findById(userDetails.getId())
                                .orElseThrow(() -> new NotFound("User not found"));

        Movie movie = movieRepository.findById(movieId)
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        Favorite favorite = Favorite.builder()
                                    .user(user)
                                    .movie(movie)
                                    .build();

        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponseDTO> getUserFavorites() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        return favoriteRepository.findByUserId(userDetails.getId())
                                .stream()
                                .map(f -> new FavoriteResponseDTO(
                                    f.getMovie().getId(),
                                    f.getMovie().getTitle(),
                                    f.getMovie().getPosterUrl()
                                ))
                                .toList();
    }

    public List<Favorite> getAll() {
        return favoriteRepository.findAll();
    }
}
