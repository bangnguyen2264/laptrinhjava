package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.AddressDto;
import com.project.ShopKoi.model.form.AddressForm;
import com.project.ShopKoi.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public AddressDto createAddress(@RequestBody @Valid AddressForm address) {
        return addressService.createAddress(address);
    }
    @GetMapping("/{addressId}")
    public AddressDto getAddressById(@PathVariable Long addressId) {
        return addressService.getAddressById(addressId);
    }


}
