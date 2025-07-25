package com.umutbasket.berkyazici.repository;

import com.umutbasket.berkyazici.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}