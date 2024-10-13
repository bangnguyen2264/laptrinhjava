package com.project.ShopKoi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")  // Tạo cột `address_id` trong bảng `address_item`
    private List<AddressItem> addressItems;

    @PrePersist
    @PreUpdate
    public void validateAddressItems() {
        if (addressItems == null || addressItems.size() != 3) {
            throw new IllegalStateException("Mỗi địa chỉ phải có đúng 3 AddressItem.");
        }
    }
}
