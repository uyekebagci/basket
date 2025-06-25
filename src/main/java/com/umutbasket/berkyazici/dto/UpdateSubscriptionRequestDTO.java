package com.umutbasket.berkyazici.dto;

import lombok.Data;

@Data
public class UpdateSubscriptionRequestDTO {
    private String newPlanType;
    private Boolean isActive;
}