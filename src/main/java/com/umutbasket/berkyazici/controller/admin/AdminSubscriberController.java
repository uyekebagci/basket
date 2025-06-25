package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.dto.SubscriberResponseDTO;
import com.umutbasket.berkyazici.entity.Subscriber;
import com.umutbasket.berkyazici.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/subscribers")
@RequiredArgsConstructor
public class AdminSubscriberController {

    private final SubscriberService subscriberService;

    // --- Tüm metotlar artık SubscriberResponseDTO dönecek ---

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(subscribers.stream()
                .map(this::convertToSubscriberResponseDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriberResponseDTO>> getActiveSubscribers() {
        List<Subscriber> activeSubscribers = subscriberService.getAllActiveSubscribers();
        return ResponseEntity.ok(activeSubscribers.stream()
                .map(this::convertToSubscriberResponseDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/nonActive")
    public ResponseEntity<List<SubscriberResponseDTO>> getNonActiveSubscribers() {
        List<Subscriber> nonActiveSubscribers = subscriberService.getAllNonActiveSubscribers();
        return ResponseEntity.ok(nonActiveSubscribers.stream()
                .map(this::convertToSubscriberResponseDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> getSubscriberById(@PathVariable Long id) {
        return subscriberService.getSubscriberById(id)
                .map(this::convertToSubscriberResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriberResponseDTO> getSubscriberByUserId(@PathVariable Long userId) {
        return subscriberService.getSubscriberByUserId(userId)
                .map(this::convertToSubscriberResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriberResponseDTO> createSubscription(@RequestBody Map<String, Object> payload) { // TODO: Burası için de bir DTO oluşturulmalı
        try {
            Long userId = ((Number) payload.get("userId")).longValue();
            String planType = (String) payload.get("planType");

            if (userId == null || planType == null || planType.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Subscriber createdSubscriber = subscriberService.createSubscription(userId, planType);
            return new ResponseEntity<>(convertToSubscriberResponseDTO(createdSubscriber), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error creating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Diğer PUT ve DELETE metotları şimdilik aynı kalabilir,
    // ancak update metodu da DTO dönmeli.
    @PutMapping("/{subscriberId}")
    public ResponseEntity<SubscriberResponseDTO> updateSubscription(
            @PathVariable Long subscriberId,
            @RequestBody Map<String, Object> payload) { // TODO: Burası için de bir DTO oluşturulmalı
        try {
            // ... (iç mantık aynı)
            Subscriber updatedSubscriber = subscriberService.updateSubscription(subscriberId, (String) payload.get("newPlanType"), (Boolean) payload.get("isActive"), null);
            return ResponseEntity.ok(convertToSubscriberResponseDTO(updatedSubscriber));
        } catch (RuntimeException e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ... (DELETE metodu aynı kalabilir, bir şey döndürmüyor)

    // --- YARDIMCI DÖNÜŞTÜRÜCÜ METOT ---
    private SubscriberResponseDTO convertToSubscriberResponseDTO(Subscriber subscriber) {
        return SubscriberResponseDTO.builder()
                .subId(subscriber.getSubId())
                .userId(subscriber.getUser().getUserId())
                .userEmail(subscriber.getUser().getEmail())
                .subscriptionStartDate(subscriber.getSubscriptionStartDate())
                .subscriptionEndDate(subscriber.getSubscriptionEndDate())
                .isActive(subscriber.getIsActive())
                .planType(subscriber.getPlanType())
                .build();
    }
}