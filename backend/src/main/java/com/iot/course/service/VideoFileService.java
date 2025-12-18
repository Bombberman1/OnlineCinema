package com.iot.course.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.course.dto.video_file.VideoFileQualityResponseDTO;
import com.iot.course.dto.video_file.VideoFileRequestDTO;
import com.iot.course.dto.video_file.VideoFileUploadRequestDTO;
import com.iot.course.exception.Exists;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Movie;
import com.iot.course.model.VideoFile;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.VideoFileRepository;

@Service
public class VideoFileService {
    @Autowired
    private VideoFileRepository videoFileRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Value("${videos-path}")
    private String basePath;

    public void create(VideoFileRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        if (videoFileRepository.findByMovieIdAndQuality(
                dto.movieId(),
                dto.quality()
            )
            .isPresent()
        ) {
            throw new Exists("Video with this quality already exists");
        }

        if (Files.notExists(Paths.get(dto.filePath()))) {
            throw new NotFound("Video not found on server");
        }

        VideoFile video = VideoFile.builder()
                                .movie(movie)
                                .quality(dto.quality())
                                .filePath(dto.filePath())
                                .build();

        videoFileRepository.save(video);
    }

    public void uploadVideo(MultipartFile file, VideoFileUploadRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        try {
            Path movieDir = Paths.get(basePath, movie.getTitle());
            Files.createDirectories(movieDir);

            Path target = movieDir.resolve(dto.quality() + ".mp4");

            Files.copy(
                file.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING
            );

            videoFileRepository.findByMovieIdAndQuality(movie.getId(), dto.quality())
                            .ifPresent(videoFileRepository::delete);

            VideoFile video = VideoFile.builder()
                                    .movie(movie)
                                    .quality(dto.quality())
                                    .filePath(target.toString())
                                    .build();

            videoFileRepository.save(video);

        } catch (IOException e) {
            throw new InternalError("Failed to store video file", e);
        }
    }

    public List<VideoFileQualityResponseDTO> getAvailableQualities(Long movieId) {
        return videoFileRepository.findByMovieId(movieId)
                                .stream()
                                .map(v -> new VideoFileQualityResponseDTO(v.getQuality()))
                                .toList();
    }

    public Resource loadVideoResource(Long movieId, Integer quality) {
        VideoFile video = videoFileRepository.findByMovieIdAndQuality(movieId, quality)
                                            .orElseThrow(() -> new NotFound("Video not found or wrong quality"));

        Path path = Paths.get(video.getFilePath());

        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new NotFound("Video file not found");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new NotFound("Video file not found");
        }
    }

    public List<VideoFile> getAll() {
        return videoFileRepository.findAll();
    }
}
