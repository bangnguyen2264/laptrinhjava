package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.entity.AddressStatus;
import lombok.Data;

@Data
public class AddressItemForm {
    private String name;
    private AddressStatus status;
    private Long parentId;

}
