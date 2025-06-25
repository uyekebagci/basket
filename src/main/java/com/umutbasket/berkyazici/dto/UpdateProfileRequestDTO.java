package com.umutbasket.berkyazici.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class UpdateProfileRequestDTO {
    // Bir kullanıcının kendi profilinde güncelleyebileceği alanlar
    private String firstName;
    private String lastName;
    private Date birthDay;
    private Integer height;
    private Double weight;
    private String gender;
}