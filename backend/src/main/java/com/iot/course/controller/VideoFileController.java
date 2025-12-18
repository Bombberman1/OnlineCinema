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
import org.springframework.web.multipart.MultipartFile;

import com.iot.course.dto.video_file.VideoFileQualityResponseDTO;
import com.iot.course.dto.video_file.VideoFileRequestDTO;
import com.iot.course.dto.video_file.VideoFileUploadRequestDTO;
import com.iot.course.model.VideoFile;
import com.iot.course.service.VideoFileService;
import com.iot.course.service.WatchHistoryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/video_files")
public class VideoFileController {
    @Autowired
    private VideoFileService videoFileService;
    @Autowired
    private WatchHistoryService watchHistoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody VideoFileRequestDTO dto) {
        videoFileService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadVideo(
        @RequestPart("file") MultipartFile file,
        @RequestPart("metadata") VideoFileUploadRequestDTO dto
    ) {
        videoFileService.uploadVideo(file, dto);
        return ResponseEntity.status(201).build();
    }

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
