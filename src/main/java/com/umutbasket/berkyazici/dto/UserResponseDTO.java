package com.umutbasket.berkyazici.dto;

import com.umutbasket.berkyazici.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class UserResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthDay;
    private Integer height;
    private Double weight;
    private Integer age;
    private String gender;
    private Role role;
    private String plan;
}