package com.project.ShopKoi.model.form;

import lombok.Data;

@Data
public class UpdatePasswordForm {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}