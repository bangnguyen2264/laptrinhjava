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

    @Override
    public String toString() {
        StringBuilder addressString = new StringBuilder(name + ": ");
        for (AddressItem item : addressItems) {
            addressString.append(item.getName()).append(", ");  // `getDetail()` là giả định phương thức lấy chi tiết từ AddressItem
        }
        // Xóa dấu phẩy và khoảng trắng cuối cùng
        if (!addressString.isEmpty()) {
            addressString.setLength(addressString.length() - 2);
        }
        return addressString.toString();
    }


}
