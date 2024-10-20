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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressItemServiceImpl implements AddressItemService {

    private final AddressItemRepository addressItemRepository;

    @Override
    @Transactional
    public List<AddressItemDto> findAllAddressItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return addressItemRepository.findAll(pageable).stream()
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
        // Kiểm tra xem addressClass có hợp lệ không
        if (addressItemForm.getAddressClass() == null) {
            throw new IllegalArgumentException("Address class must not be null");
        }

        AddressItem addressItem = toEntity(addressItemForm);
        addressItemRepository.save(addressItem);
        return AddressItemDto.toDto(addressItem);
    }

    @Override
    @CacheEvict(value = "address_item", allEntries = true)
    public List<AddressItemDto> addAllAddressItem(List<AddressItemForm> addressItemForms) {
        if (addressItemForms == null || addressItemForms.isEmpty()) {
            throw new IllegalArgumentException("Address item forms must not be null or empty");
        }

        // Chuyển đổi từ AddressItemForm sang AddressItem
        List<AddressItem> addressItems = addressItemForms.stream()
                .map(this::toEntity)
                .toList();

        // Lưu tất cả vào cơ sở dữ liệu
        addressItemRepository.saveAll(addressItems);

        // Chuyển đổi và trả về AddressItemDto
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

        // Kiểm tra xem addressClass có hợp lệ không
        if (addressItemForm.getAddressClass() == null) {
            throw new IllegalArgumentException("Address class must not be null");
        }

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
