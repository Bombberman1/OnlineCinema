package com.iot.course.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.course.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
