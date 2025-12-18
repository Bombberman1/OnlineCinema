package com.iot.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.watch_history.WatchHistoryResponseDTO;
import com.iot.course.service.WatchHistoryService;

@RestController
@RequestMapping("/api/watch_history")
public class WatchHistoryController {
    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable Long movieId) {
        watchHistoryService.create(movieId);
        return ResponseEntity.status(201).build();
    }
    
    @GetMapping
    public List<WatchHistoryResponseDTO> getAll() {
        return watchHistoryService.getUserHistory();
    }
}
