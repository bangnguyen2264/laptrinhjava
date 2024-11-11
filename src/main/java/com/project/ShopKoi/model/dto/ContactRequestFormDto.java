package com.project.ShopKoi.model.dto;

import lombok.Data;

@Data
public class ContactRequestFormDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String job;
    private String title;
    private String description;
}
