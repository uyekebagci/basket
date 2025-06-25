package com.umutbasket.berkyazici.mapper;

import com.umutbasket.berkyazici.dto.UpdateUserRequestDTO;
import com.umutbasket.berkyazici.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper {

    /**
     * Admin tarafından gönderilen DTO ile mevcut bir kullanıcıyı günceller.
     * @param request Güncelleme verilerini içeren DTO.
     * @param user Veritabanından bulunan ve güncellenecek olan User entity'si.
     */
    public void updateUserFromDto(UpdateUserRequestDTO request, User user) {
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getBirthDay() != null) {
            user.setBirthDay(request.getBirthDay());
        }
        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            user.setWeight(request.getWeight());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getPlan() != null) {
            user.setPlan(request.getPlan());
        }
    }
}