package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.entity.Subscriber;
import com.umutbasket.berkyazici.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/subscribers")
public class AdminSubscriberController {

    private final SubscriberService subscriberService;

    @Autowired
    public AdminSubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    // This endpoint will return ALL subscribers (active and inactive)
    @GetMapping
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(subscribers);
    }

    // NEW ENDPOINT: This endpoint will return ONLY active subscribers
    @GetMapping("/active")
    public ResponseEntity<List<Subscriber>> getActiveSubscribers() {
        List<Subscriber> activeSubscribers = subscriberService.getAllActiveSubscribers();
        return ResponseEntity.ok(activeSubscribers);
    }

    @GetMapping("/nonActive")
    public ResponseEntity<List<Subscriber>> getNonActiveSubscribers() {
        List<Subscriber> nonActiveSubscribers = subscriberService.getAllNonActiveSubscribers();
        return ResponseEntity.ok(nonActiveSubscribers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> getSubscriberById(@PathVariable Long id) {
        return subscriberService.getSubscriberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Subscriber> getSubscriberByUserId(@PathVariable Long userId) {
        return subscriberService.getSubscriberByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Subscriber> createSubscription(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = ((Number) payload.get("userId")).longValue();
            String planType = (String) payload.get("planType");

            if (userId == null || planType == null || planType.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            Subscriber createdSubscriber = subscriberService.createSubscription(userId, planType);
            return new ResponseEntity<>(createdSubscriber, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Log the exception for debugging purposes
            System.err.println("Error creating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{subscriberId}")
    public ResponseEntity<Subscriber> updateSubscription(
            @PathVariable Long subscriberId,
            @RequestBody Map<String, Object> payload) {
        try {
            String newPlanType = (String) payload.get("newPlanType");
            Boolean isActive = (Boolean) payload.get("isActive");
            LocalDateTime subscriptionEndDate = null;
            if (payload.containsKey("subscriptionEndDate") && payload.get("subscriptionEndDate") != null) {
                subscriptionEndDate = LocalDateTime.parse((String) payload.get("subscriptionEndDate"));
            }

            Subscriber updatedSubscriber = subscriberService.updateSubscription(
                    subscriberId, newPlanType, isActive, subscriptionEndDate);
            return ResponseEntity.ok(updatedSubscriber);
        } catch (RuntimeException e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/cancel/{subscriberId}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long subscriberId) {
        try {
            subscriberService.cancelSubscription(subscriberId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("Error cancelling subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        try {
            subscriberService.deleteSubscriber(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("Error deleting subscriber: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}