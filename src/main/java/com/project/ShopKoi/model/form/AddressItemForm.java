package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.enums.AddressClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressItemForm {
    @NotBlank
    private String name;
    @NotBlank
    private AddressClass addressClass;
    private Long parentId;

}
