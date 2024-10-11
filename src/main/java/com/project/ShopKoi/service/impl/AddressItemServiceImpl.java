package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.service.AddressItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressItemServiceImpl  implements AddressItemService {

    private final AddressItemRepository addressItemRepository;

    @Override
    public List<AddressItemDto> findAllAddressItems() {
        return addressItemRepository.findAll().stream().map(AddressItemDto::toDto).toList();
    }

    @Override
    public AddressItemDto findAddressItemById(Long id) {
        AddressItem addressItem = addressItemRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Address item with id " + id + " not found"));
        return AddressItemDto.toDto(addressItem);
    }

    @Override
    public List<AddressItemDto> addAllAddressItem(List<AddressItemForm> addressItemForms) {
        List<AddressItem> addressItems = addressItemForms.stream()
                .map(this::toEntity)
                .toList();
        return addressItems.stream().map(AddressItemDto::toDto).toList();
    }

    @Override
    public List<AddressItemDto> findAddressItemByParentId(Long parentId) {
        return List.of();
    }

    @Override
    public AddressItemDto firstAddressItemByChildrenId(Long childrenId) {
        return null;
    }

    @Override
    public AddressItemDto updateAddressItem(AddressItemForm addressItem) {
        return null;
    }

    @Override
    public void deleteAddressItem(Long id) {

    }

    private AddressItem toEntity(AddressItemForm addressItemForm) {
        AddressItem parentItem = addressItemRepository
                .findById(addressItemForm.getParentId())
                .orElseThrow(()-> new UsernameNotFoundException("Address item with parent id " + addressItemForm.getParentId() + " not found"));

        return AddressItem.builder()
                .name(addressItemForm.getName())
                .addressStatus(addressItemForm.getStatus())
                .parent(parentItem)
                .build();
    }
}
