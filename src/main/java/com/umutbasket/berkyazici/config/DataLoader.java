package com.umutbasket.berkyazici.config;

import com.umutbasket.berkyazici.entity.Plan;
import com.umutbasket.berkyazici.repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadPlans(PlanRepository planRepository) {
        return args -> {
            if (planRepository.count() == 0) {
                planRepository.save(new Plan(null, "Starter Plan", "1 Aylık Giriş Planı", 49.99, false, 30));
                planRepository.save(new Plan(null, "Pro Plan", "3 Aylık Profesyonel Plan", 119.99, false, 90));
                planRepository.save(new Plan(null, "Elite Plan", "Sınırsız Erişim Planı", 199.99, true, null));
                planRepository.save(new Plan(null, "Trial Plan", "7 Günlük Deneme Planı", 0.0, false, 7));
            }
        };
    }
}