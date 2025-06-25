package com.umutbasket.berkyazici.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriberResponseDTO {
    private Long subId;
    private Long userId; // User nesnesi yerine sadece ID'si
    private String userEmail; // Kullanıcının email'ini de eklemek faydalı olabilir
    private LocalDateTime subscriptionStartDate;
    private LocalDateTime subscriptionEndDate;
    private Boolean isActive;
    private String planType;
}