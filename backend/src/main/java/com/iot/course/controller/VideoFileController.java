package com.iot.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(
        value = "/{movieId}/watch",
        produces = "application/vnd.apple.mpegurl"
    )
    public String watch(@PathVariable Long movieId, @RequestParam Integer quality) {
        return videoFileService.loadHlsPlaylist(movieId, quality);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<VideoFile> getAllVideoFiles() {
        return videoFileService.getAll();
    }
}
