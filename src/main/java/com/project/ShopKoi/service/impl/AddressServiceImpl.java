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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;

    @Override
    @CachePut(value = "address", key = "#result.id")
    public AddressDto createAddress(AddressForm addressForm) {
        Address address = buildAddressFromForm(addressForm);
        addressRepository.save(address);
        return AddressDto.toDto(address);
    }

    @Override
    @Cacheable(value = "address", key = "#id")
    public AddressDto getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(AddressDto::toDto)
                .orElseThrow(() -> new NotFoundException("Address not found"));
    }

    @Override
    @CachePut(value = "address", key = "#result.id")
    public AddressDto updateAddress(Long id, AddressForm addressForm) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found"));

        updateAddressFromForm(address, addressForm);
        addressRepository.save(address);
        return AddressDto.toDto(address);
    }

    @Override
    @CacheEvict(value = "address", key = "#id")
    @Transactional
    public void deleteAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found"));

        // Xóa các AddressItem liên quan trước
        address.setAddressItems(new ArrayList<>());

        addressRepository.save(address);

        // Sau đó xóa Address
        addressRepository.deleteById(id);
    }


    private Address buildAddressFromForm(AddressForm addressForm) {
        AddressItem country = getAddressItemByName(addressForm.getCountry(), "Country");
        AddressItem city = getAddressItemByName(addressForm.getCity(), "City");
        AddressItem district = getAddressItemByName(addressForm.getDistrict(), "District");

        return Address.builder()
                .name(addressForm.getName())
                .addressItems(List.of(district, city, country))
                .build();
    }

    private void updateAddressFromForm(Address address, AddressForm addressForm) {
        AddressItem country = getAddressItemByName(addressForm.getCountry(), "Country");
        AddressItem city = getAddressItemByName(addressForm.getCity(), "City");
        AddressItem district = getAddressItemByName(addressForm.getDistrict(), "District");

        address.setName(addressForm.getName());
        address.getAddressItems().clear();
        address.getAddressItems().addAll(List.of(district, city, country));
    }

    private AddressItem getAddressItemByName(String name, String itemType) {
        return addressItemRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(itemType + " " + name + " not found"));
    }
}
