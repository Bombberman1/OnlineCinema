package com.iot.course.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.iot.course.exception.NotFound;
import com.iot.course.model.Movie;
import com.iot.course.model.User;
import com.iot.course.model.WatchHistory;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.repository.WatchHistoryRepository;
import com.iot.course.util.CustomUserDetails;

@Service
public class WatchHistoryService {
    @Autowired
    private WatchHistoryRepository watchHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public void create(Long movieId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                                .orElseThrow(() -> new NotFound("User not found"));

        Movie movie = movieRepository.findById(movieId)
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        watchHistoryRepository.save(
            WatchHistory.builder()
                        .user(user)
                        .movie(movie)
                        .watchedAt(LocalDateTime.now())
                        .build()
        );
    }
}
