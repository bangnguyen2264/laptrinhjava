package com.project.ShopKoi.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.enums.AddressClass;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateInformationUserForm {
    @NotBlank
    private String fullName;
    @NotNull
    @Past
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    @NotBlank
    private String phone;

    public static User toEntity(UpdateInformationUserForm updateInformationUserForm) {
        return User.builder()
                .fullName(updateInformationUserForm.getFullName())
                .dob(updateInformationUserForm.getDob())
                .phone(updateInformationUserForm.getPhone())
                .build();
    }

}
