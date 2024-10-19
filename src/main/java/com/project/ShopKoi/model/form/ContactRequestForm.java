package com.project.ShopKoi.model.form;

import lombok.Data;

@Data
public class ContactRequestForm {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String job;
    private String title;
    private String description;
}

