package com.iot.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.video_file.VideoFileQualityResponseDTO;
import com.iot.course.model.VideoFile;
import com.iot.course.service.VideoFileService;
import com.iot.course.service.WatchHistoryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/video_files")
public class VideoFileController {
    @Autowired
    private VideoFileService videoFileService;
    @Autowired
    private WatchHistoryService watchHistoryService;

    @GetMapping("/{movieId}/qualities")
    public List<VideoFileQualityResponseDTO> getQualities(@PathVariable Long movieId) {
        return videoFileService.getAvailableQualities(movieId);
    }

    @GetMapping("/{movieId}/watch")
    public ResponseEntity<Resource> watch(@PathVariable Long movieId, @RequestParam Integer quality) {
        Resource videoResource = videoFileService.loadVideoResource(movieId, quality);

        watchHistoryService.create(movieId);

        return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                            .body(videoResource);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<VideoFile> getAllVideoFiles() {
        return videoFileService.getAll();
    }
}
