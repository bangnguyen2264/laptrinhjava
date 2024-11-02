package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.ContactRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactRequestDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String job;
    private String title;
    private String description;


    public static ContactRequestDto toDto(ContactRequest request) {
        return ContactRequestDto.builder()
                .id(request.getId())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .job(request.getJob())
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }
}
