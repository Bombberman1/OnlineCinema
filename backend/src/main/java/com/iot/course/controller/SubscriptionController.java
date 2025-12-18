package com.iot.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.course.dto.subscription.SubscriptionRequestDTO;
import com.iot.course.dto.subscription.SubscriptionResponseDTO;
import com.iot.course.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody SubscriptionRequestDTO dto) {
        subscriptionService.create(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public SubscriptionResponseDTO getCurrentSubscription() {
        return subscriptionService.getUserSubscription();
    }
}
