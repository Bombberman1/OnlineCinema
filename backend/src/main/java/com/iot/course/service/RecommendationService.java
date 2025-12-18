package com.iot.course.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.course.model.Genre;
import com.iot.course.model.Movie;
import com.iot.course.model.WatchHistory;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.WatchHistoryRepository;

@Service
public class RecommendationService {
    @Autowired
    private WatchHistoryRepository watchHistoryRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> recommend(Long userId) {
        List<WatchHistory> history = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);

        if (history.isEmpty()) {
            return movieRepository.findAll();
        }

        Map<Long, Integer> genreCounter = new HashMap<>();
        Set<Long> watchedMovies = new HashSet<>();

        for (WatchHistory h : history) {
            watchedMovies.add(h.getMovie().getId());
            for (Genre g : h.getMovie().getGenres()) {
                genreCounter.merge(g.getId(), 1, Integer::sum);
            }
        }

        List<Long> topGenres = genreCounter.entrySet()
                                        .stream()
                                        .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                                        .limit(3)
                                        .map(Map.Entry::getKey)
                                        .toList();

        return movieRepository.findAll()
                            .stream()
                            .filter(m -> !watchedMovies.contains(m.getId()))
                            .filter(m -> m.getGenres()
                                        .stream()
                                        .anyMatch(g -> topGenres.contains(g.getId()))
                            )
                            .toList();
    }
}
