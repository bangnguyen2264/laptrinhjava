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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;

    @Override
    public AddressDto createAddress(AddressForm addressForm) {
        AddressItem country = addressItemRepository
                .findByName(addressForm.getCountry())
                .orElseThrow(()->new NotFoundException("Country "+addressForm.getCountry()+" not found"));
        AddressItem city = addressItemRepository
                .findByName(addressForm.getCity())
                .orElseThrow(()->new NotFoundException("Country "+addressForm.getCity()+" not found"));
        AddressItem district = addressItemRepository
                .findByName(addressForm.getDistrict())
                .orElseThrow(()->new NotFoundException("Country "+addressForm.getCity()+" not found"));
        Address address = Address.builder()
                .name(addressForm.getName())
                .addressItems(new ArrayList<>())
                .build();
        address.getAddressItems().addAll(List.of(district,country, city ));
        addressRepository.save(address);
        return AddressDto.toDto(address);
    }

    @Override
    public AddressDto getAddressById(Long id) {
        return AddressDto
                .toDto(addressRepository
                        .findById(id)
                        .orElseThrow(()->new NotFoundException("Address not found")));
    }

    @Override
    public AddressDto updateAddress(AddressForm addressForm) {
        return null;
    }

    @Override
    public void deleteAddressById(Long id) {

    }
}
