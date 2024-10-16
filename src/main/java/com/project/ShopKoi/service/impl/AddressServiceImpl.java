package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.model.dto.AddressDto;
import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.form.AddressForm;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.repository.AddressRepository;
import com.project.ShopKoi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;

    @Override
    @Cacheable(value = "address")
    public AddressDto createAddress(AddressForm addressForm) {
        AddressItem country = addressItemRepository
                .findByName(addressForm.getCountry())
                .orElseThrow(() -> new NotFoundException("Country " + addressForm.getCountry() + " not found"));
        AddressItem city = addressItemRepository
                .findByName(addressForm.getCity())
                .orElseThrow(() -> new NotFoundException("City " + addressForm.getCity() + " not found"));
        AddressItem district = addressItemRepository
                .findByName(addressForm.getDistrict())
                .orElseThrow(() -> new NotFoundException("District " + addressForm.getDistrict() + " not found"));

        Address address = Address.builder()
                .name(addressForm.getName())
                .addressItems(new ArrayList<>())
                .build();
        address.getAddressItems().addAll(List.of(district, city,country));
        addressRepository.save(address);
        return AddressDto.toDto(address);
    }

    @Override
    @Cacheable(value = "address", key = "#id")
    public AddressDto getAddressById(Long id) {
        return AddressDto
                .toDto(addressRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("Address not found")));
    }

    @Override
    @Cacheable(value = "address")
    public AddressDto updateAddress(AddressForm addressForm) {
        // Logic cập nhật Address
        return null;
    }

    @Override
    @CacheEvict(value = "address", key = "#id")
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }
}
