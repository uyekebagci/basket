package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.entity.Subscriber;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.SubscriberRepository;
import com.umutbasket.berkyazici.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubscriberService(SubscriberRepository subscriberRepository, UserRepository userRepository) {
        this.subscriberRepository = subscriberRepository;
        this.userRepository = userRepository;
    }

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    // New method to get only active subscribers
    public List<Subscriber> getAllActiveSubscribers() {
        return subscriberRepository.findByIsActiveTrue();
    }

    public List<Subscriber> getAllNonActiveSubscribers() {
        return subscriberRepository.findByIsActiveFalse();
    }

    public Optional<Subscriber> getSubscriberById(Long id) {
        return subscriberRepository.findById(id);
    }

    public Optional<Subscriber> getSubscriberByUserId(Long userId) {
        return subscriberRepository.findByUserUserId(userId);
    }

    @Transactional
    public Subscriber createSubscription(Long userId, String planType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (user.getSubscriber() != null && user.getSubscriber().getIsActive()) {
            throw new RuntimeException("User already has an active subscription.");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setSubscriptionStartDate(LocalDateTime.now());
        subscriber.setIsActive(true);
        subscriber.setPlanType(planType);

        user.setSubscriber(subscriber);
        userRepository.save(user);

        return subscriber;
    }

    @Transactional
    public Subscriber updateSubscription(Long subscriberId, String newPlanType, Boolean isActive, LocalDateTime subscriptionEndDate) {
        Subscriber existingSubscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        if (newPlanType != null && !newPlanType.isEmpty()) {
            existingSubscriber.setPlanType(newPlanType);
        }
        if (isActive != null) {
            existingSubscriber.setIsActive(isActive);
        }
        if (subscriptionEndDate != null) {
            existingSubscriber.setSubscriptionEndDate(subscriptionEndDate);
        }

        return subscriberRepository.save(existingSubscriber);
    }

    @Transactional
    public void cancelSubscription(Long subscriberId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        subscriber.setIsActive(false);
        subscriber.setSubscriptionEndDate(LocalDateTime.now());
        subscriberRepository.save(subscriber);
    }

    @Transactional
    public void deleteSubscriber(Long id) {
        // Find the subscriber first to also unset the link on the User side if necessary,
        // though orphanRemoval=true should handle this if you were to null out the user.subscriber field.
        // Direct deletion will remove the subscriber record.
        subscriberRepository.deleteById(id);
    }
}