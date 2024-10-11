package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.entity.AddressStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressItemDto {
    private Long id;
    private String name;
    private Long parentId;

    public static AddressItemDto toDto(AddressItem address) {
        return AddressItemDto.builder()
                .id(address.getId())
                .name(address.getName())
                .parentId(address.getParent().getId())
                .build();
    }
}
