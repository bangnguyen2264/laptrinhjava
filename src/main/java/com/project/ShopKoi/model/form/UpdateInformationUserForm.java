package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.enums.AddressClass;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateInformationUserForm {
    private String fullName;
    private String phone;
    private String address;

}
