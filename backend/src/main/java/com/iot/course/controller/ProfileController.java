package com.iot.course.controller;

import com.iot.course.dto.profile.ProfileRequestDTO;
import com.iot.course.dto.profile.ProfileResponseDTO;
import com.iot.course.model.Profile;
import com.iot.course.service.ProfileService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ProfileRequestDTO dto) {
        profileService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/current")
    public ProfileResponseDTO getCurrentProfile() {
        return profileService.getUserProfile();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAll();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProfileRequestDTO dto) {
        profileService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
