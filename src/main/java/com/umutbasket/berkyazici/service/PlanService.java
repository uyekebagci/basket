
package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.entity.Plan;
import com.umutbasket.berkyazici.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }
}
