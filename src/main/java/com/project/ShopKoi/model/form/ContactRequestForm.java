package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.entity.ContactRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactRequestForm {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @Size(max = 50, message = "Job title must be less than 50 characters")
    private String job;

    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    public static ContactRequest toEntity(ContactRequestForm contactRequest) {
        return ContactRequest.builder()
                .fullName(contactRequest.getFullName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .job(contactRequest.getJob())
                .title(contactRequest.getTitle())
                .description(contactRequest.getDescription())
                .build();
    }
}
