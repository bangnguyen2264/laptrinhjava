package com.project.ShopKoi.model.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressForm {
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String name;
}
