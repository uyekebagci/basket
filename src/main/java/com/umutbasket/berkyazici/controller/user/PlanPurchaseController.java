
package com.umutbasket.berkyazici.controller.user;

import com.umutbasket.berkyazici.dto.PurchasePlanRequestDTO;
import com.umutbasket.berkyazici.entity.Subscriber;
import com.umutbasket.berkyazici.service.subscriber.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/plans")
public class PlanPurchaseController {

    private final SubscriberService subscriberService;

    public PlanPurchaseController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Subscriber> purchasePlan(@RequestBody PurchasePlanRequestDTO request) {
        System.out.println(">>> purchasePlan endpoint triggered!");
        Subscriber subscriber = subscriberService.purchasePlan(request.getUserId(), request.getPlanId());
        return ResponseEntity.ok(subscriber);
    }
}
