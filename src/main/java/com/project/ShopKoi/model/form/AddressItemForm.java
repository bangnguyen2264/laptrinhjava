package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.enums.AddressClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressItemForm {
    private String name;             // Tên của địa chỉ
    private AddressClass addressClass; // Enum cho địa chỉ
    private double longitude;        // Kinh độ
    private double latitude;         // Vĩ độ
    private Long parentId;          // ID của địa chỉ cha
}
