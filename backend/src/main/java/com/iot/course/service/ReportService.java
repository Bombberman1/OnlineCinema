package com.iot.course.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.course.dto.report.PopularGenreDTO;
import com.iot.course.dto.report.PopularMovieDTO;
import com.iot.course.dto.report.ReportDTO;
import com.iot.course.dto.report.SubscriptionRevenueDTO;
import com.iot.course.dto.report.UserActivityDTO;
import com.iot.course.model.Movie;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.SubscriptionRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.repository.WatchHistoryRepository;

@Service
public class ReportService {
    @Autowired
    private WatchHistoryRepository watchHistoryRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private static final int TOP_LIMIT = 10;
    private static final int DAYS_FOR_ACTIVITY = 30;

    public ReportDTO generateReport() {
        List<PopularMovieDTO> popularMovies = getPopularMovies();
        List<PopularGenreDTO> popularGenres = getPopularGenres();
        UserActivityDTO userActivity = getUserActivity();
        SubscriptionRevenueDTO subscriptionRevenue = getSubscriptionRevenue();

        return new ReportDTO(
            popularMovies,
            popularGenres,
            userActivity,
            subscriptionRevenue
        );
    }

    private List<PopularMovieDTO> getPopularMovies() {
        List<Object[]> results = watchHistoryRepository.findMovieViewCounts();
        
        return results.stream()
                    .limit(TOP_LIMIT)
                    .map(result -> {
                        Long movieId = ((Number) result[0]).longValue();
                        Long viewCount = ((Number) result[1]).longValue();
                        
                        Movie movie = movieRepository.findById(movieId)
                            .orElse(null);
                        
                        if (movie == null) {
                            return null;
                        }
                        
                        return new PopularMovieDTO(
                            movieId,
                            movie.getTitle(),
                            viewCount
                        );
                    })
                    .filter(movie -> movie != null)
                    .collect(Collectors.toList());
    }

    private List<PopularGenreDTO> getPopularGenres() {
        List<Object[]> results = watchHistoryRepository.findGenreViewCounts();
        
        return results.stream()
                    .limit(TOP_LIMIT)
                    .map(result -> {
                        Long genreId = ((Number) result[0]).longValue();
                        String genreName = (String) result[1];
                        Long viewCount = ((Number) result[2]).longValue();
                        
                        return new PopularGenreDTO(
                            genreId,
                            genreName,
                            viewCount
                        );
                    })
                    .collect(Collectors.toList());
    }

    private UserActivityDTO getUserActivity() {
        LocalDateTime since = LocalDateTime.now().minusDays(DAYS_FOR_ACTIVITY);
        
        Long totalUsers = userRepository.count();
        Long activeUsers = watchHistoryRepository.countDistinctUsersSince(since);
        Long totalViews = watchHistoryRepository.countViewsSince(since);
        
        return new UserActivityDTO(
            totalUsers,
            activeUsers,
            totalViews,
            LocalDateTime.now()
        );
    }

    private SubscriptionRevenueDTO getSubscriptionRevenue() {
        List<com.iot.course.model.Subscription> subscriptions = subscriptionRepository.findAll();
        
        Long totalSubscriptions = (long) subscriptions.size();
        
        BigDecimal totalRevenue = subscriptions.stream()
                                            .map(com.iot.course.model.Subscription::getPrice)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageRevenue = totalSubscriptions > 0
            ? totalRevenue.divide(BigDecimal.valueOf(totalSubscriptions), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        return new SubscriptionRevenueDTO(
            totalSubscriptions,
            totalRevenue,
            averageRevenue
        );
    }
}

