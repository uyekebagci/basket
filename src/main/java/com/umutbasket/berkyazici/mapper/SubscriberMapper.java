package com.umutbasket.berkyazici.mapper;

import com.umutbasket.berkyazici.dto.SubscriberResponseDTO;
import com.umutbasket.berkyazici.entity.Subscriber;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriberMapper {

    // Tek bir Subscriber nesnesini SubscriberResponseDTO'ya çevirir
    public SubscriberResponseDTO toSubscriberResponseDTO(Subscriber subscriber) {
        return SubscriberResponseDTO.builder()
                .subId(subscriber.getSubId())
                .userId(subscriber.getUser().getUserId())
                .userEmail(subscriber.getUser().getEmail())
                .subscriptionStartDate(subscriber.getSubscriptionStartDate())
                .subscriptionEndDate(subscriber.getSubscriptionEndDate())
                .isActive(subscriber.getIsActive())
                .planType(subscriber.getPlanType())
                .build();
    }

    // Bir Subscriber listesini SubscriberResponseDTO listesine çevirir
    public List<SubscriberResponseDTO> toSubscriberResponseDTOList(List<Subscriber> subscribers) {
        return subscribers.stream()
                .map(this::toSubscriberResponseDTO)
                .collect(Collectors.toList());
    }
}