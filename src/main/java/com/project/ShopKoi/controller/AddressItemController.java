package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.service.AddressItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address-items")
@RequiredArgsConstructor
public class AddressItemController {
    private final AddressItemService addressItemService;

    @GetMapping
    public ResponseEntity<List<AddressItemDto>> getAllAddressItems() {
        List<AddressItemDto> addressItems = addressItemService.findAllAddressItems();
        return ResponseEntity.ok(addressItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressItemDto> getAddressItemById(@PathVariable Long id) {
        AddressItemDto addressItem = addressItemService.findAddressItemById(id);
        return ResponseEntity.ok(addressItem);
    }

    @PostMapping
    public ResponseEntity<AddressItemDto> addAddressItem(@RequestBody AddressItemForm addressItemForm) {
        AddressItemDto newAddressItem = addressItemService.addAddressItem(addressItemForm);
        return ResponseEntity.ok(newAddressItem);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AddressItemDto>> addAllAddressItems(@RequestBody List<AddressItemForm> addressItemForms) {
        List<AddressItemDto> newAddressItems = addressItemService.addAllAddressItem(addressItemForms);
        return ResponseEntity.ok(newAddressItems);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<AddressItemDto>> getAddressItemsByParentId(@PathVariable Long parentId) {
        List<AddressItemDto> addressItems = addressItemService.findAddressItemByParentId(parentId);
        return ResponseEntity.ok(addressItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressItemDto> updateAddressItem(@PathVariable Long id, @RequestBody AddressItemForm addressItemForm) {
        AddressItemDto updatedAddressItem = addressItemService.updateAddressItem(addressItemForm);
        return ResponseEntity.ok(updatedAddressItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressItem(@PathVariable Long id) {
        addressItemService.deleteAddressItem(id);
        return ResponseEntity.noContent().build();
    }
}
