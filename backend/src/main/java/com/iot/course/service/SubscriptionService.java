package com.iot.course.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.iot.course.dto.subscription.SubscriptionRequestDTO;
import com.iot.course.dto.subscription.SubscriptionResponseDTO;
import com.iot.course.exception.Exists;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Subscription;
import com.iot.course.model.User;
import com.iot.course.repository.SubscriptionRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.util.CustomUserDetails;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.findByUserId(userId)
                                    .filter(sub -> sub.getEndDate().isAfter(LocalDate.now()))
                                    .isPresent();
    }

    public SubscriptionResponseDTO getUserSubscription() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        Subscription sub = subscriptionRepository.findByUserId(userDetails.getId())
                                                .orElseThrow(() -> new NotFound("No subscription"));

        return new SubscriptionResponseDTO(
            sub.getStartDate(),
            sub.getEndDate(),
            sub.getPrice()
        );
    }

    public void create(SubscriptionRequestDTO dto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        Long userId = userDetails.getId();
    
        if (subscriptionRepository.existsByUserId(userId)) {
            throw new Exists("Subscription already exists");
        }

        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFound("User not found"));

        Subscription sub = Subscription.builder()
            .user(user)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(dto.days()))
            .price(dto.price())
            .build();

        subscriptionRepository.save(sub);
    }
}
