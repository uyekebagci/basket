
package com.umutbasket.berkyazici.dto;

public class PurchasePlanRequestDTO {
    private Long userId;
    private Long planId;

    public PurchasePlanRequestDTO() {}

    public PurchasePlanRequestDTO(Long userId, Long planId) {
        this.userId = userId;
        this.planId = planId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
