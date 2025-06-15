package com.umutbasket.berkyazici.repository;

import com.umutbasket.berkyazici.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import List
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByUserUserId(Long userId);

    List<Subscriber> findByIsActiveTrue();

    List<Subscriber> findByIsActiveFalse();
}