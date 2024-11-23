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

    private double longitude;
    private double latitude;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "address_address_item", // Tên bảng liên kết
            joinColumns = @JoinColumn(name = "address_id"), // Cột trong bảng liên kết
            inverseJoinColumns = @JoinColumn(name = "address_item_id") // Cột trong bảng liên kết
    )
    private List<AddressItem> addressItems;

    @PrePersist
    @PreUpdate
    public void setCoordinatesFromFirstItem() {
        if (addressItems == null || addressItems.size() != 3) {
            throw new IllegalStateException("Mỗi địa chỉ phải có đúng 3 AddressItem.");
        }
        AddressItem firstItem = addressItems.get(0);
        this.longitude = firstItem.getLongitude();
        this.latitude = firstItem.getLatitude();
    }

    @Override
    public String toString() {
        StringBuilder addressString = new StringBuilder(name + " ");
        for (AddressItem item : addressItems) {
            addressString.append(item.getName()).append(", ");
        }
        if (!addressString.isEmpty()) {
            addressString.setLength(addressString.length() - 2);
        }
        return addressString.toString();
    }
}

