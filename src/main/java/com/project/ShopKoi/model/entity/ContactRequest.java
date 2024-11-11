package com.project.ShopKoi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {
    @Id
    @GeneratedValue
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String job;
    private String title;
    private String description;
}
