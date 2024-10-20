package com.project.ShopKoi.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordForm {
    @NotBlank
    private String oldPassword;
    @NotBlank
    @Size(min = 8)
    private String newPassword;
    @NotBlank
    @Size(min = 8)
    private String confirmPassword;
}