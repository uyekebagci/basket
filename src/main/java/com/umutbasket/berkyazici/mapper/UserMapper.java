package com.umutbasket.berkyazici.mapper;

import com.umutbasket.berkyazici.dto.UserResponseDTO;
import com.umutbasket.berkyazici.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // Bu sınıfın bir Spring Bean'i olmasını sağlıyoruz
public class UserMapper {

    // Tek bir User nesnesini UserResponseDTO'ya çevirir
    public UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthDay(user.getBirthDay())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(user.getAge())
                .gender(user.getGender())
                .role(user.getRole())
                .plan(user.getPlan())
                .build();
    }

    // Bir User listesini UserResponseDTO listesine çevirir
    public List<UserResponseDTO> toUserResponseDTOList(List<User> users) {
        return users.stream()
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
    }
}