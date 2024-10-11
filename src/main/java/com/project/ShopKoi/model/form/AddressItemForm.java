package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.entity.AddressClass;
import lombok.Data;

@Data
public class AddressItemForm {
    private String name;
    private AddressClass addressClass;
    private Long parentId;

}