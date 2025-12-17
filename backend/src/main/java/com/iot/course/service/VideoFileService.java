package com.iot.course.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.iot.course.dto.video_file.VideoFileQualityResponseDTO;
import com.iot.course.exception.NotFound;
import com.iot.course.model.VideoFile;
import com.iot.course.repository.VideoFileRepository;

@Service
public class VideoFileService {
    @Autowired
    private VideoFileRepository videoFileRepository;

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
