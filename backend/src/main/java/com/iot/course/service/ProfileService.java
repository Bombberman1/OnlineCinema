package com.iot.course.service;

import com.iot.course.dto.profile.ProfileRequestDTO;
import com.iot.course.dto.profile.ProfileResponseDTO;
import com.iot.course.exception.Exists;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Profile;
import com.iot.course.model.User;
import com.iot.course.repository.ProfileRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    public void create(ProfileRequestDTO dto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        Long userId = userDetails.getId();

        if (profileRepository.findByUserId(userId).isPresent()) {
            throw new Exists("Profile already exists");
        }

        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFound("User not found"));

        Profile profile = Profile.builder()
                                .user(user)
                                .username(dto.username())
                                .avatarUrl(dto.avatarUrl())
                                .birthDate(dto.birthDate())
                                .country(dto.country())
                                .build();

        profileRepository.save(profile);
    }

    public ProfileResponseDTO getUserProfile() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        Profile profile = profileRepository.findByUserId(userDetails.getId())
                                        .orElseThrow(() -> new NotFound("Profile not found"));

        return new ProfileResponseDTO(
            profile.getUsername(),
            profile.getAvatarUrl(),
            profile.getBirthDate(),
            profile.getCountry()
        );
    }

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    public void update(ProfileRequestDTO dto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        Profile profile = profileRepository.findByUserId(userDetails.getId())
                                        .orElseThrow(() -> new NotFound("Profile not found"));

        profile.setUsername(dto.username());
        profile.setAvatarUrl(dto.avatarUrl());
        profile.setBirthDate(dto.birthDate());
        profile.setCountry(dto.country());

        profileRepository.save(profile);
    }
}
