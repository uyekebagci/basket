package com.umutbasket.berkyazici.dto;

import com.umutbasket.berkyazici.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date birthDay;
    private Integer height;
    private Double weight;
    private String gender;
}