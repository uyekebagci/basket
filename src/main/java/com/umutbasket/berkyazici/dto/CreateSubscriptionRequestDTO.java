package com.umutbasket.berkyazici.dto;

import lombok.Data;

@Data
public class CreateSubscriptionRequestDTO {
    private Long userId;
    private String planType;
    private Long planId;
    private int durationInDays; // örnek: 30 gün
}