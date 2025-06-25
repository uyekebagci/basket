package com.umutbasket.berkyazici.dto;

import com.umutbasket.berkyazici.entity.Role;
import lombok.Data;

import java.sql.Date;

@Data
public class UpdateUserRequestDTO {
    // Admin'in bir kullanıcının hangi bilgilerini güncelleyebileceğini burada tanımlarız.
    private String firstName;
    private String lastName;
    private String email;
    // Admin'in parola değiştirmesine izin veriyor muyuz? Şimdilik hayır.
    // private String password;
    private Date birthDay;
    private Integer height;
    private Double weight;
    private String gender;
    private Role role; // Admin, kullanıcının rolünü değiştirebilir.
    private String plan;
}