package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.AddressDto;
import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.form.AddressForm;

public interface AddressService {
    AddressDto createAddress(AddressForm addressForm);
    AddressDto getAddressById(Long id);
    AddressDto updateAddress(AddressForm addressForm);
    void deleteAddressById(Long id);

}
