package com.project.ShopKoi.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInForm {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
