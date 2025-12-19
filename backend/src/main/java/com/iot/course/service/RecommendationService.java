package com.iot.course.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static final int RECOMMEND_LIMIT = 4;

    public List<Movie> recommend(Long userId) {
        List<WatchHistory> history = watchHistoryRepository.findTop20ByUserIdOrderByWatchedAtDesc(userId);

        Set<Long> watchedMovieIds = history.stream()
                                        .map(h -> h.getMovie().getId())
                                        .collect(Collectors.toSet());

        if (history.isEmpty()) {
            return movieRepository.findAll()
                                .stream()
                                .sorted(Comparator.comparing(Movie::getRating).reversed())
                                .limit(RECOMMEND_LIMIT)
                                .toList();
        }

        Map<Long, Integer> genreCounter = new HashMap<>();

        for (WatchHistory h : history) {
            for (Genre g : h.getMovie().getGenres()) {
                genreCounter.merge(g.getId(), 1, Integer::sum);
            }
        }

        if (genreCounter.isEmpty()) {
            return movieRepository.findAll()
                                .stream()
                                .filter(m -> !watchedMovieIds.contains(m.getId()))
                                .sorted(Comparator.comparing(Movie::getRating).reversed())
                                .limit(RECOMMEND_LIMIT)
                                .toList();
        }

        List<Long> topGenreIds = genreCounter.entrySet()
                                            .stream()
                                            .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                                            .map(Map.Entry::getKey)
                                            .toList();

        List<Movie> recommended = movieRepository.findAll()
                                                .stream()
                                                .filter(m -> !watchedMovieIds.contains(m.getId()))
                                                .filter(m -> m.getGenres()
                                                            .stream()
                                                            .anyMatch(g -> topGenreIds.contains(g.getId()))
                                                )
                                                .sorted(Comparator.comparing(Movie::getRating).reversed())
                                                .limit(RECOMMEND_LIMIT)
                                                .toList();

        if (recommended.isEmpty()) {
            return movieRepository.findAll()
                                .stream()
                                .filter(m -> !watchedMovieIds.contains(m.getId()))
                                .sorted(Comparator.comparing(Movie::getRating).reversed())
                                .limit(RECOMMEND_LIMIT)
                                .toList();
        }

        if (recommended.size() < RECOMMEND_LIMIT) {
            recommended.addAll(
                movieRepository.findAll()
                            .stream()
                            .filter(m -> !watchedMovieIds.contains(m.getId()))
                            .sorted(Comparator.comparing(Movie::getRating).reversed())
                            .limit(RECOMMEND_LIMIT - recommended.size())
                            .toList()
            );
        }

        return recommended;
    }
}
