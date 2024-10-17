package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.AddressItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressItemDto implements Serializable {
    private Long id;
    private String name;
    private Long parentId;

    public static AddressItemDto toDto(AddressItem address) {
        return AddressItemDto.builder()
                .id(address.getId())
                .name(address.getName())
                .parentId(address.getParent() != null ? address.getParent().getId() : null)
                .build();
    }
}
