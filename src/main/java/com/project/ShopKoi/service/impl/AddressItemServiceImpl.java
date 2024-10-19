package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.service.AddressItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressItemServiceImpl implements AddressItemService {

    private final AddressItemRepository addressItemRepository;

    @Override
    @Cacheable(value = "address_item")
    public List<AddressItemDto> findAllAddressItems() {
        return addressItemRepository.findAll().stream()
                .map(AddressItemDto::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "address_item", key = "#id")
    public AddressItemDto findAddressItemById(Long id) {
        return addressItemRepository.findById(id)
                .map(AddressItemDto::toDto)
                .orElseThrow(() -> new NotFoundException("Address item with id " + id + " not found"));
    }

    @Override
    @CachePut(value = "address_item", key = "#result.id")
    public AddressItemDto addAddressItem(AddressItemForm addressItemForm) {
        AddressItem addressItem = toEntity(addressItemForm);
        addressItemRepository.save(addressItem);
        return AddressItemDto.toDto(addressItem);
    }

    @Override
    @CacheEvict(value = "address_item", allEntries = true)
    public List<AddressItemDto> addAllAddressItem(List<AddressItemForm> addressItemForms) {
        List<AddressItem> addressItems = addressItemForms.stream()
                .map(this::toEntity)
                .toList();
        addressItemRepository.saveAll(addressItems);
        return addressItems.stream()
                .map(AddressItemDto::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "address_item", key = "#parentId")
    public List<AddressItemDto> findAddressItemByParentId(Long parentId) {
        return addressItemRepository.findAddressItemByParentId(parentId)
                .stream()
                .map(AddressItemDto::toDto)
                .toList();
    }

    @Override
    public List<AddressItemDto> findAllAddressItemByAddressClass(AddressClass addressClass, Long parentId) {
        return (parentId != null)
                ? addressItemRepository.findAddressItemByAddressClassAndParentId(addressClass, parentId)
                .stream()
                .map(AddressItemDto::toDto)
                .toList()
                : addressItemRepository.findAddressItemByAddressClass(addressClass)
                .stream()
                .map(AddressItemDto::toDto)
                .toList();
    }

    @Override
    @CachePut(value = "address_item", key = "#result.id")
    public AddressItemDto updateAddressItem(Long id, AddressItemForm addressItemForm) {
        AddressItem addressItem = addressItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address item with id " + id + " not found"));
        updateAddressItemFromForm(addressItem, addressItemForm);
        addressItemRepository.save(addressItem);
        return AddressItemDto.toDto(addressItem);
    }

    @Override
    @CacheEvict(value = "address_item", key = "#id")
    public void deleteAddressItem(Long id) {
        AddressItem addressItem = addressItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address item with id " + id + " not found"));
        addressItemRepository.delete(addressItem);
    }

    private AddressItem toEntity(AddressItemForm addressItemForm) {
        return AddressItem.builder()
                .name(addressItemForm.getName())
                .addressClass(addressItemForm.getAddressClass())
                .longitude(addressItemForm.getLongitude())
                .latitude(addressItemForm.getLatitude())
                .parent(getParentItem(addressItemForm.getParentId()))
                .build();
    }

    private void updateAddressItemFromForm(AddressItem addressItem, AddressItemForm addressItemForm) {
        addressItem.setName(addressItemForm.getName());
        addressItem.setAddressClass(addressItemForm.getAddressClass());
        addressItem.setLongitude(addressItemForm.getLongitude());
        addressItem.setLatitude(addressItemForm.getLatitude());
        addressItem.setParent(getParentItem(addressItemForm.getParentId()));
    }

    private AddressItem getParentItem(Long parentId) {
        return parentId == null ? null :
                addressItemRepository.findById(parentId)
                        .orElseThrow(() -> new NotFoundException("Address item with parent id " + parentId + " not found"));
    }
}

