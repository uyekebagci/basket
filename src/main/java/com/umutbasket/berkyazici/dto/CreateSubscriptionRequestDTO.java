package com.umutbasket.berkyazici.dto;

import lombok.Data;

@Data
public class CreateSubscriptionRequestDTO {
    private Long userId;
    private String planType;
}