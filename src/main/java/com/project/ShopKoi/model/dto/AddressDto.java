package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.Address;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddressDto {
    private Long id;
    private String name;
    private List<AddressItemDto> addressDtoList;

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .name(address.getName())
                .addressDtoList(address.getAddressItems().stream().map(AddressItemDto::toDto).toList())
                .build();
    }
}
