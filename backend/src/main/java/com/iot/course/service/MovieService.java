package com.iot.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.iot.course.dto.movie.MovieRequestDTO;
import com.iot.course.dto.movie.MovieResponseDTO;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Genre;
import com.iot.course.model.Movie;
import com.iot.course.repository.MovieRepository;
import com.iot.course.util.CustomUserDetails;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreService genreService;
    @Autowired
    private RecommendationService recommendationService;

    public void create(MovieRequestDTO dto) {
        Movie movie = Movie.builder()
            .title(dto.title())
            .description(dto.description())
            .releaseYear(dto.releaseYear())
            .country(dto.country())
            .rating(dto.rating())
            .posterUrl(dto.posterUrl())
            .genres(dto.genres()
                    .stream()
                    .map(genreService::getOrCreate)
                    .toList()
            )
            .build();

        movieRepository.save(movie);
    }

    public MovieResponseDTO getById(Long id) {
        Movie movie = movieRepository.findById(id)
                                    .orElseThrow(() -> new NotFound("Movie not found"));
        return new MovieResponseDTO(
            movie.getId(),
            movie.getTitle(),
            movie.getDescription(),
            movie.getReleaseYear(),
            movie.getCountry(),
            movie.getRating(),
            movie.getPosterUrl(),
            movie.getGenres()
                .stream()
                .map(Genre::getName)
                .toList()
        );
    }

    public List<MovieResponseDTO> getAll() {
        return movieRepository.findAll()
                            .stream()
                            .map(m -> new MovieResponseDTO(
                                m.getId(),
                                m.getTitle(),
                                m.getDescription(),
                                m.getReleaseYear(),
                                m.getCountry(),
                                m.getRating(),
                                m.getPosterUrl(),
                                m.getGenres()
                                    .stream()
                                    .map(Genre::getName)
                                    .toList()
                            ))
                            .toList();
    }

    public List<MovieResponseDTO> getAllRecommends() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        List<Movie> movies = recommendationService.recommend(userDetails.getId());

        return movies.stream()
                    .map(m -> new MovieResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDescription(),
                        m.getReleaseYear(),
                        m.getCountry(),
                        m.getRating(),
                        m.getPosterUrl(),
                        m.getGenres()
                            .stream()
                            .map(Genre::getName)
                            .toList()
                    ))
                    .toList();
    }
}
