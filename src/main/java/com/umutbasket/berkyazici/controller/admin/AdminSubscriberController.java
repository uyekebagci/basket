package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.dto.CreateSubscriptionRequestDTO;
import com.umutbasket.berkyazici.dto.SubscriberResponseDTO;
import com.umutbasket.berkyazici.dto.UpdateSubscriptionRequestDTO;
import com.umutbasket.berkyazici.entity.Subscriber;
import com.umutbasket.berkyazici.mapper.SubscriberMapper; // YENİ IMPORT
import com.umutbasket.berkyazici.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/subscribers")
@RequiredArgsConstructor
public class AdminSubscriberController {

    private final SubscriberService subscriberService;
    private final SubscriberMapper subscriberMapper;
    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(subscriberMapper.toSubscriberResponseDTOList(subscribers));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriberResponseDTO>> getActiveSubscribers() {
        List<Subscriber> activeSubscribers = subscriberService.getAllActiveSubscribers();
        return ResponseEntity.ok(subscriberMapper.toSubscriberResponseDTOList(activeSubscribers));
    }

    @GetMapping("/nonActive")
    public ResponseEntity<List<SubscriberResponseDTO>> getNonActiveSubscribers() {
        List<Subscriber> nonActiveSubscribers = subscriberService.getAllNonActiveSubscribers();
        return ResponseEntity.ok(subscriberMapper.toSubscriberResponseDTOList(nonActiveSubscribers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> getSubscriberById(@PathVariable Long id) {
        return subscriberService.getSubscriberById(id)
                .map(subscriberMapper::toSubscriberResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriberResponseDTO> getSubscriberByUserId(@PathVariable Long userId) {
        return subscriberService.getSubscriberByUserId(userId)
                .map(subscriberMapper::toSubscriberResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriberResponseDTO> createSubscription(@RequestBody CreateSubscriptionRequestDTO request) {
        try {
            Subscriber createdSubscriber = subscriberService.createSubscription(request.getUserId(), request.getPlanType());
            return new ResponseEntity<>(subscriberMapper.toSubscriberResponseDTO(createdSubscriber), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error creating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // TODO: Global exception handling
        }
    }

    @PutMapping("/{subscriberId}")
    public ResponseEntity<SubscriberResponseDTO> updateSubscription(
            @PathVariable Long subscriberId,
            @RequestBody UpdateSubscriptionRequestDTO request) {
        try {
            // Servis metodunu da sadece DTO alacak şekilde basitleştirebiliriz, şimdilik böyle kalabilir.
            Subscriber updatedSubscriber = subscriberService.updateSubscription(subscriberId, request.getNewPlanType(), request.getIsActive(), null);
            return ResponseEntity.ok(subscriberMapper.toSubscriberResponseDTO(updatedSubscriber));
        } catch (RuntimeException e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // TODO: Global exception handling
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        subscriberService.deleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }
}