package com.iot.course.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.course.repository.SubscriptionRepository;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.findByUserId(userId)
                                    .filter(sub -> sub.getEndDate().isAfter(LocalDate.now()))
                                    .isPresent();
    }
}
