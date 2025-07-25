
package com.umutbasket.berkyazici.controller;

import com.umutbasket.berkyazici.entity.Plan;
import com.umutbasket.berkyazici.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public List<Plan> getAllPlans() {
        return planService.getAllPlans();
    }
}
